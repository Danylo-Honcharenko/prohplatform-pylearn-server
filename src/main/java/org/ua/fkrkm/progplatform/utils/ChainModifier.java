package org.ua.fkrkm.progplatform.utils;

import java.util.function.Consumer;

/**
 * Ланцюг модифікацій
 *
 * @param <T> тип для модифікації
 */
public class ChainModifier<T> {
    // Об'єкт який модифікуємо
    private final T object;

    /**
     * Конструктор
     *
     * @param object об'єкт
     */
    protected ChainModifier(T object) {
        this.object = object;
    }

    /**
     * Ініціалізація
     *
     * @param object об'єкт який модифікуємо
     * @param <T> тип для модифікації
     * @return ChainModifier поточний об'єкт
     */
    public static <T> ChainModifier<T> init(T object) {
        return new ChainModifier<>(object);
    }

    /**
     * Застосувати зміни до об'єкта
     *
     * @param modifier метод застосування змін
     * @return ChainModifier поточний об'єкт
     */
    public ChainModifier<T> apply(Consumer<T> modifier) {
        modifier.accept(object);
        return this;
    }

    /**
     * Отримати змінений об'єкт
     *
     * @return T змінений об'єкт
     */
    public T get() {
        return object;
    }
}
