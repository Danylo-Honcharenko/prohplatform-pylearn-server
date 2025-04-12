package org.ua.fkrkm.progplatform.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.RoleServiceI;

import java.util.List;

/**
 * Сервіс для роботи з ролями
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleServiceI {

    private final RoleDaoI roleDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAllRoleResponse getAll() {
        List<Role> roles = roleDao.getAll();
        return new GetAllRoleResponse(roles);
    }
}
