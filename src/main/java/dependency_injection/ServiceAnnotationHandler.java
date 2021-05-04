package dependency_injection;

public class ServiceAnnotationHandler implements AnnotationHandler {

    private final ObjectFactory objectFactory;

    public ServiceAnnotationHandler(final ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public void handle(final Class<?> annotatedClass) {
        objectFactory.get(annotatedClass);
    }
}
