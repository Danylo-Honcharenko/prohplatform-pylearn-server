package org.ua.fkrkm.progplatform.utils;

import java.util.function.Consumer;

/**
 * Клас модифікації об'єкта
 *
 * @param <T> тип об'єкта
 */
public class ObjectModifier<T> {
    // Об'єкт
    private final T object;

    /**
     * Конструктор
     *
     * @param object об'єкт
     */
    private ObjectModifier(T object) {
        this.object = object;
    }

    /**
     * Метод ініціалізації
     *
     * @param object об'єкт
     * @param <T> типа об'єкта
     * @return ObjectModifier<T> поточний клас
     */
    public static <T> ObjectModifier<T> init(T object) {
        return new ObjectModifier<>(object);
    }

    /**
     * Застосувати зміни до об'єкта
     *
     * @param action дія застосування змін
     * @return ObjectModifier<T> поточний клас
     */
    public ObjectModifier<T> apply(Consumer<T> action) {
        action.accept(object);
        return this;
    }

    /**
     * Отримати об'єкт
     *
     * @return T об'єкт
     */
    public T get() {
        return object;
    }
}
