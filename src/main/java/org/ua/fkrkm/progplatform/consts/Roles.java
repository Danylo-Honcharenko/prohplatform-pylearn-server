package org.ua.fkrkm.progplatform.consts;

import lombok.Getter;

/**
 * Ролі користувачів
 */
@Getter
public enum Roles {
    /**
     * Звичайний користувач
     */
    USER("ROLE_USER"),
    /**
     * Вчитель
     */
    TEACHER("ROLE_TEACHER"),
    /**
     * Адміністратор
     */
    ADMIN("ROLE_ADMIN");

    // Назва ролі
    private final String roleName;

    /**
     * Конструктор
     *
     * @param roleName назва ролі
     */
    Roles(String roleName) {
        this.roleName = roleName;
    }
}
