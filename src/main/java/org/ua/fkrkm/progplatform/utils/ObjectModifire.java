package org.ua.fkrkm.progplatform.utils;

import java.util.function.Consumer;

public class ObjectModifire<T> {

    private final T object;

    private ObjectModifire(T object) {
        this.object = object;
    }

    public static <T> ObjectModifire<T> init(T object) {
        return new ObjectModifire<>(object);
    }

    public ObjectModifire<T> apply(Consumer<T> action) {
        action.accept(object);
        return this;
    }

    public T get() {
        return object;
    }
}
