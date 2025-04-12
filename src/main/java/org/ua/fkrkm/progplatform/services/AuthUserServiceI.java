package org.ua.fkrkm.progplatform.services;

import org.ua.fkrkm.proglatformdao.entity.User;

/**
 * Інтерфейс сервісу для роботи з поточним користувачем в системі
 */
public interface AuthUserServiceI {
    /**
     * Отримати роширену сутність поточного користувача в системі
     *
     * @return UserExt розширена сутність користувача
     */
    User getCurrentAuthUser();
    /**
     * Перевіряє що поточний користувач администратор
     *
     * @return boolean true/false
     */
    boolean isCurrentAuthUserAdmin();
    /**
     * Перевіряє що поточний користувач вчитель
     *
     * @return boolean true/false
     */
    boolean isCurrentAuthUserTeacher();
}
