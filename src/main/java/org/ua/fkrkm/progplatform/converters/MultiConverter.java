package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Конвертор з можливістю конвертації списків
 *
 * @param <S> джерело
 * @param <T> призначення
 */
public interface MultiConverter<S, T> extends Converter<S, T> {
    /**
     * Конвертувати список
     *
     * @param source список джерело
     * @return List<T> список призначення
     */
    @Nullable
    List<T> convert(List<S> source);
}
