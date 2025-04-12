package org.ua.fkrkm.progplatform.services;

import org.ua.fkrkm.progplatformclientlib.response.*;

/**
 * Інтерфейс для роботи з ролями
 */
public interface RoleServiceI {
    /**
     * Отримати всі ролі
     *
     * @return GetAllRolesResponse відповідь API
     */
    GetAllRoleResponse getAll();
}
