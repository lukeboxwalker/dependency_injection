package com.lukeboxwalker.processing;

public interface ObjectFactory {

    boolean contains(final String className);

    boolean contains(final Class<?> componentClass);

    Object get(final String className);

    <T> T get(final Class<T> componentClass);
}
