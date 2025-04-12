package org.ua.fkrkm.progplatform.converters;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

/**
 * Клас конвертор "User" в "CreateUserResponse"
 */
@Component
@RequiredArgsConstructor
public class UserToCreateUserResponse implements Converter<User, CreateUserResponse> {
    // DAO для роботи з ролями
    private final RoleDaoI roleDao;
    // Логування
    private final static Logger logger = LoggerFactory.getLogger(UserToCreateUserResponse.class);

    /**
     * Метод конвертації
     *
     * @param source вхідна сутність
     * @return CreateUserResponse перетворена
     */
    @Override
    public CreateUserResponse convert(User source) {
        return CreateUserResponse.builder()
                .id(source.getId())
                .firstName(source.getFirst_name())
                .lastName(source.getLast_name())
                .email(source.getEmail())
                .role(getRoleName(source.getRoleId()))
                .created(source.getCreated())
                .build();
    }

    /**
     * Метод для отримання назви ролі
     *
     * @param roleId ID ролі
     * @return String назва ролі
     */
    private String getRoleName(int roleId) {
        Role role = roleDao.getById(roleId);
        if (role == null) {
            logger.warn("Не знайдено ролі з ID: {}", roleId);
            throw new ProgPlatformException("Не знайдено ролі з ID: " + roleId);
        }
        return role.getName();
    }
}
