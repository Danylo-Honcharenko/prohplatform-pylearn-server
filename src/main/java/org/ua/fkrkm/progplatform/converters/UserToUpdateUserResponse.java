package org.ua.fkrkm.progplatform.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.response.*;

/**
 * Клас конвертор "User" в "UpdateUserResponse"
 */
@Component
@RequiredArgsConstructor
public class UserToUpdateUserResponse implements Converter<User, UpdateUserResponse> {

    // DAO для роботи з ролями
    private final RoleDaoI roleDao;

    @Override
    public UpdateUserResponse convert(User source) {
        // Отримуємо роль по ID
        Role role = roleDao.getById(source.getRoleId());
        return UpdateUserResponse.builder()
                .firstName(source.getFirst_name())
                .lastName(source.getLast_name())
                .email(source.getEmail())
                .role(role.getName())
                .created(source.getCreated())
                .updated(source.getUpdated())
                .build();
    }
}
