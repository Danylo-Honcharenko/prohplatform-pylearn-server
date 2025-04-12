package org.ua.fkrkm.progplatform.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.request.*;

import java.util.Date;

/**
 * Клас конвертор "CreateUserRequest" в "User"
 */
@Component
@RequiredArgsConstructor
public class UserRequestToUser implements Converter<UserRegistrationRequest, User> {
    // DAO для роботи з ролями
    private final RoleDaoI roleDao;
    // Інтерфейс для створення захешованого паролю
    private final PasswordEncoder passwordEncoder;

    /**
     * Метод конвертації
     *
     * @param source вхідна сутність
     * @return User перетворена
     */
    @Override
    public User convert(UserRegistrationRequest source) {
        return User.builder()
                .first_name(source.getFirstName())
                .last_name(source.getLastName())
                .email(source.getEmail())
                .password(getHashPassword(source.getPassword()))
                .roleId(getRoleId())
                .created(new Date())
                .build();
    }

    /**
     * Метод для отримання захешованого паролю
     *
     * @param password пароль для хешування
     * @return String захешований пароль
     */
    private String getHashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Метод для отримання ID ролі
     *
     * @return Integer ID знайденої ролі
     */
    private Integer getRoleId() {
        return roleDao.findIdByName("ROLE_USER").getFirst();
    }
}
