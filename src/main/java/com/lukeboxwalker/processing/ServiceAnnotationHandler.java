package com.lukeboxwalker.processing;

public class ServiceAnnotationHandler implements ComponentAnnotationHandler {

    private final ComponentFactory objectFactory;

    public ServiceAnnotationHandler(final ComponentFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public void handle(final Class<?> annotatedClass) {
        objectFactory.get(annotatedClass);
    }
}
