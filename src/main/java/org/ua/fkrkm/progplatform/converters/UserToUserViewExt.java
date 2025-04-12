package org.ua.fkrkm.progplatform.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.proglatformdao.entity.view.UserView;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

/**
 * Конвертація "User" в "UserViewExt"
 */
@Component
@RequiredArgsConstructor
public class UserToUserViewExt implements Converter<User, UserView> {

    // DAO для роботи з ролями
    private final RoleDaoI roleDao;

    @Override
    public UserView convert(User source) {
        return UserView.builder()
                .id(source.getId())
                .firstName(source.getFirst_name())
                .lastName(source.getLast_name())
                .email(source.getEmail())
                .role(getRoleNameById(source.getRoleId()))
                .created(source.getCreated())
                .build();
    }

    /**
     * Отримати ім'я ролі по ID
     *
     * @param id ID ролі
     * @return String ім'я олі
     */
    private String getRoleNameById(int id) {
        try {
            Role role = roleDao.getById(id);
            return role.getName();
        } catch (IncorrectResultSizeDataAccessException ex) {
            throw new ProgPlatformException(ErrorConsts.ROLE_NOT_FOUND);
        }
    }
}
