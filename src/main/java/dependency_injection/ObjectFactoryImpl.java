package dependency_injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
final class ObjectFactoryImpl implements ObjectFactory {

    private static final String UNCHECKED = "unchecked";

    private final Map<Class<?>, Object> singletonBeans = new HashMap<>();

    /* default */ ObjectFactoryImpl() {
        super();
        provide(this);
        singletonBeans.remove(ObjectFactoryImpl.class);
    }

    public void provide(final Object object) {
        singletonBeans.put(object.getClass(), object);
        addSuperAndInterfaces(object.getClass(), object);
    }

    @Override
    public boolean contains(final String className) {
        try {
            return singletonBeans.containsKey(Class.forName(className));
        } catch (ClassNotFoundException cause) {
            throw new ObjectCreationException(cause);
        }
    }

    @Override
    public boolean contains(final Class<?> componentClass) {
        return singletonBeans.containsKey(componentClass);
    }

    @Override
    public Object get(final String className) {
        try {
            return get(Class.forName(className), new HashSet<>());
        } catch (ClassNotFoundException cause) {
            throw new ObjectCreationException(cause);
        }
    }

    @Override
    public <T> T get(final Class<T> component) {
        return get(component, new HashSet<>());
    }

    private <T> T get(final Class<T> componentClass, final Set<Class<?>> dependencies) {
        return get(componentClass, componentClass, dependencies);
    }

    @SuppressWarnings(UNCHECKED)
    private <T> T get(final Class<?> original, final Class<T> component, final Set<Class<?>> dependencies) {
        if (ObjectFactoryImpl.class.equals(component)) {
            return (T) get(ObjectFactory.class);
        }
        if (contains(component)) {
            return (T) singletonBeans.get(component);
        } else {
            if (component.isAnnotationPresent(Service.class)) {
                return createObject(original, component, dependencies);
            } else {
                return createDependency(component);
            }
        }
    }

    @SuppressWarnings(UNCHECKED)
    private <T> T createObject(final Class<?> original, final Class<T> component, final Set<Class<?>> dependencies) {
        dependencies.add(component);
        final Optional<Constructor<?>> autowiredConstructor = Arrays.stream(component.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst();
        Constructor<?> beanConstructor;
        try {
            if (autowiredConstructor.isPresent()) {
                beanConstructor = autowiredConstructor.get();
            } else {
                beanConstructor = component.getDeclaredConstructor();
            }
            beanConstructor.setAccessible(true);
            final Class<?>[] paramClasses = beanConstructor.getParameterTypes();
            final Object[] params = new Object[paramClasses.length];
            for (int i = 0; i < paramClasses.length; i++) {
                if (dependencies.contains(paramClasses[i])) {
                    throw new ObjectCreationException("Could not create component with circular dependency: " +
                            original.getSimpleName() + " ⮀ " + component.getSimpleName());
                }
                params[i] = get(original, paramClasses[i], dependencies);
            }
            final T object = (T) beanConstructor.newInstance(params);
            singletonBeans.put(component, object);
            addSuperAndInterfaces(component, object);
            return object;
        } catch (ReflectiveOperationException cause) {
            throw new ObjectCreationException(cause);
        }
    }

    private void addSuperAndInterfaces(final Class<?> componentClass, Object object) {
        final Class<?>[] interfaces = componentClass.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            if (singletonBeans.containsKey(interfaceClass)) {
                singletonBeans.remove(interfaceClass);
            } else {
                singletonBeans.put(interfaceClass, object);
            }
        }
        final Class<?> superClass = componentClass.getSuperclass();
        if (!superClass.equals(Object.class)) {
            if (singletonBeans.containsKey(superClass)) {
                singletonBeans.remove(superClass);
            } else {
                singletonBeans.put(superClass, object);
            }
            addSuperAndInterfaces(superClass, object);
        }

    }

    private <T> T createDependency(final Class<T> dependencyClass) throws ObjectCreationException {
        try {
            final Constructor<T> constructor = dependencyClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException cause) {
            throw new ObjectCreationException(cause);
        }
    }
}
