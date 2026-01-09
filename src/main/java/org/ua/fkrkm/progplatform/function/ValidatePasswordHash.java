package org.ua.fkrkm.progplatform.function;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.request.*;
import java.util.function.Predicate;

/**
 * Валідація хешу паролів
 */
public class ValidatePasswordHash implements Predicate<UserLoginRequest> {
    // Користувач
    private final User user;
    // Інтерфейс для створення захешованого паролю
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор
     *
     * @param passwordEncoder інтерфейс для створення захешованого паролю
     * @param user користувач
     */
    public ValidatePasswordHash(PasswordEncoder passwordEncoder, User user) {
        this.passwordEncoder = passwordEncoder;
        this.user = user;
    }

    @Override
    public boolean test(UserLoginRequest candidate) {
        // Перевіряємо, що паролі з запиту і з бази однакові
        return passwordEncoder.matches(candidate.getPassword(), user.getPassword());
    }
}
