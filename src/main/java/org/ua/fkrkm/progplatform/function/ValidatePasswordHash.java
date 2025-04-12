package org.ua.fkrkm.progplatform.function;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

import java.util.List;
import java.util.function.Predicate;

/**
 * Валідація хешу паролів
 */
public class ValidatePasswordHash implements Predicate<UserLoginRequest> {
    // DAO для роботи з користувачами
    private final UserDaoI userDao;
    // Інтерфейс для створення захешованого паролю
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор
     *
     * @param passwordEncoder інтерфейс для створення захешованого паролю
     * @param userDao DAO для роботи з користувачами
     */
    public ValidatePasswordHash(PasswordEncoder passwordEncoder, UserDaoI userDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    @Override
    public boolean test(UserLoginRequest candidate) {
        // Отримуємо користувача з бази
        List<User> users = userDao.findByEmail(candidate.getEmail());
        // Перевіряємо що він існує
        if (users.isEmpty()) throw new ProgPlatformException(ErrorConsts.USER_NOT_FOUND);
        User user = users.getFirst();
        // Перевіряємо що паролі з запиту і з бази однакові
        return passwordEncoder.matches(candidate.getPassword(), user.getPassword());
    }
}
