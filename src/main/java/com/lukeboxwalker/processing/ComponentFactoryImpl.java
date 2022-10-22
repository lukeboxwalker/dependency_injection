package com.lukeboxwalker.processing;

import com.lukeboxwalker.processing.annotation.AutowiredConstructor;
import com.lukeboxwalker.processing.annotation.PluginComponent;
import com.lukeboxwalker.processing.exception.DependencyLookUpException;
import com.lukeboxwalker.processing.exception.ObjectCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@PluginComponent
final class ComponentFactoryImpl implements ComponentFactory {

    private static final String UNCHECKED = "unchecked";

    private final Map<Class<?>, Object> singletons = new HashMap<>();

    /* default */ ComponentFactoryImpl() {
        super();
        addSuperAndInterfaces(ComponentFactoryImpl.class, this);
    }

    public void provide(final Object object) {
        singletons.put(object.getClass(), object);
        addSuperAndInterfaces(object.getClass(), object);
    }

    @Override
    public boolean contains(final String className) {
        try {
            return singletons.containsKey(Class.forName(className));
        } catch (ClassNotFoundException cause) {
            throw new DependencyLookUpException(cause);
        }
    }

    @Override
    public boolean contains(final Class<?> componentClass) {
        return singletons.containsKey(componentClass);
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
        if (ComponentFactoryImpl.class.equals(component)) {
            return (T) get(ComponentFactory.class);
        }
        if (contains(component)) {
            return (T) singletons.get(component);
        }
        return create(original, component, dependencies);

    }

    private <T> T create(final Class<?> original, final Class<T> component, final Set<Class<?>> dependencies) {
        if (component.isAnnotationPresent(PluginComponent.class)) {
            return createObject(original, component, dependencies);
        } else {
            return createDependency(component);
        }
    }

    @SuppressWarnings(UNCHECKED)
    private <T> T createObject(final Class<?> original, final Class<T> component, final Set<Class<?>> dependencies) {
        System.out.println(component);
        dependencies.add(component);
        final Optional<Constructor<?>> autowiredConstructor = Arrays.stream(component.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(AutowiredConstructor.class))
                .findFirst();
        Constructor<T> beanConstructor;
        try {
            if (autowiredConstructor.isPresent()) {
                beanConstructor = (Constructor<T>) autowiredConstructor.get();
            } else {
                beanConstructor = component.getDeclaredConstructor();
            }
            beanConstructor.setAccessible(true);
            final Class<?>[] paramClasses = beanConstructor.getParameterTypes();
            final Object[] params = new Object[paramClasses.length];
            for (int i = 0; i < paramClasses.length; i++) {
                if (dependencies.contains(paramClasses[i])) {
                    throw new ObjectCreationException("Could not create component with circular dependency between: " +
                            original.getSimpleName() + " and " + component.getSimpleName());
                }
                params[i] = get(original, paramClasses[i], dependencies);
            }
            final T object = beanConstructor.newInstance(params);
            singletons.put(component, object);
            addSuperAndInterfaces(component, object);
            return object;
        } catch (ReflectiveOperationException cause) {
            throw new ObjectCreationException(cause);
        }
    }

    private void addSuperAndInterfaces(final Class<?> componentClass, Object object) {
        final Class<?>[] interfaces = componentClass.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            if (singletons.containsKey(interfaceClass)) {
                singletons.remove(interfaceClass);
            } else {
                singletons.put(interfaceClass, object);
            }
        }
        final Class<?> superClass = componentClass.getSuperclass();
        if (!superClass.equals(Object.class)) {
            if (singletons.containsKey(superClass)) {
                singletons.remove(superClass);
            } else {
                singletons.put(superClass, object);
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
