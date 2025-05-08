package org.ua.fkrkm.progplatform.exceptions;

import java.util.Map;

/**
 * Помилки
 */
public class ErrorConsts {
    /**
     * Не знайдено користувача
     */
    public final static ErrorCfg USER_NOT_FOUND = new ErrorCfg(Map.of(
            "uk", "Користувача не знайдено!",
            "ru", "Пользователь не найден!",
            "en", "User not found!"
    ));
    /**
     * Не знайдено курс
     */
    public final static ErrorCfg COURSE_NOT_FOUND = new ErrorCfg(Map.of(
            "uk", "Курс не знайдено!",
            "ru", "Курс не найден!",
            "en", "Course not found!"
    ));
    /**
     * Не знайдено тему
     */
    public final static ErrorCfg TOPIC_NOT_FOUND = new ErrorCfg(Map.of(
            "uk", "Тему не знайдено!",
            "ru", "Тему не найдено!",
            "en", "Topic not found!"
    ));
    /**
     * Не знайдено даних в базі
     */
    public final static ErrorCfg DATA_NOT_FOUND = new ErrorCfg(Map.of(
            "uk", "Дані не знайдено в базі!",
            "ru", "Данные не найдены в базе!",
            "en", "Data not found in database!"
    ));
    /**
     * Не знайдено ролі
     */
    public final static ErrorCfg ROLE_NOT_FOUND = new ErrorCfg(Map.of(
            "uk", "Роль не знайдена!",
            "ru", "Роль не найдена!",
            "en", "Role not found!"
    ));
    /**
     * Не знайдено тесту
     */
    public final static ErrorCfg TEST_NOT_FOUND = new ErrorCfg(Map.of(
            "uk", "Тест не знайдено!",
            "ru", "Тест не найден!",
            "en", "Test not found!"
    ));
    /**
     * Не знайдено модуль
     */
    public final static ErrorCfg MODULE_NOT_FOUND = new ErrorCfg(Map.of(
            "uk", "Модуль не знайдено!",
            "ru", "Модуль не найден!",
            "en", "Module not found!"
    ));
    /**
     * Неможливо видалити іншого користувача, крім нього самого
     */
    public final static ErrorCfg CANNOT_DELETE_ANOTHER_USER = new ErrorCfg(Map.of(
            "uk", "Неможливо видалити іншого користувача, крім вас самих!",
            "ru", "Невозможно удалить другого пользователя, кроме вас самих!",
            "en", "It is impossible to delete another user except yourself!"
    ));
    /**
     * Неправильно вказано пароль
     */
    public final static ErrorCfg PASSWORD_IS_INCORRECT = new ErrorCfg(Map.of(
            "uk", "Неправильно вказано пароль!",
            "ru", "Не верно указан пароль!",
            "en", "The password is incorrect!"
    ));
}
