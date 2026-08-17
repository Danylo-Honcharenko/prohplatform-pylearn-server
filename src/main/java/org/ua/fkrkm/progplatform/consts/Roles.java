package org.ua.fkrkm.progplatform.consts;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Ролі користувачів
 */
@Getter
public enum Roles {
    /**
     * Звичайний користувач
     */
    USER(1L, "ROLE_USER"),
    /**
     * Вчитель
     */
    TEACHER(2L, "ROLE_TEACHER"),
    /**
     * Адміністратор
     */
    ADMIN(3L , "ROLE_ADMIN");

    // ID ролі
    private final Long id;
    // Назва ролі
    private final String roleName;

    /**
     * Конструктор
     *
     * @param id ID ролі
     * @param roleName назва ролі
     */
    Roles(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    /**
     * Отримати назву ролі по ID ролі
     *
     * @param id ID ролі
     * @return String назва ролі
     */
    public static String getNameById(Long id) {
       return Arrays.stream(Roles.values())
               .filter(role -> role.getId().equals(id))
               .findFirst()
               .map(Roles::getRoleName)
               .orElse(StringUtils.EMPTY);
    }
}
