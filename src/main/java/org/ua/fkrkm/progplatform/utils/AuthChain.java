package org.ua.fkrkm.progplatform.utils;

import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Ланцюг аутентифікації
 */
public class AuthChain {
    // Запит
    private final UserLoginRequest user;

    /**
     * Конструктор
     *
     * @param user запит
     */
    public AuthChain(UserLoginRequest user) {
        this.user = user;
    }

    /**
     * Метод ініціалізації
     *
     * @param user запит
     * @return AuthChain ланцюг аутентифікації
     */
    public static AuthChain init(UserLoginRequest user) {
        return new AuthChain(user);
    }

    /**
     * Застосувати зміни до об'єкта
     *
     * @param action дія
     * @return AuthChain ланцюг аутентифікації
     */
    public AuthChain apply(Consumer<UserLoginRequest> action) {
        action.accept(user);
        return this;
    }

    /**
     * Перевірка умови
     *
     * @param predicate умова
     * @return AuthChain ланцюг аутентифікації
     */
    public AuthChain check(Predicate<UserLoginRequest> predicate) {
        boolean test = predicate.test(user);
        if (!test) throw new ProgPlatformException("Неправильний пароль!");
        return this;
    }

    /**
     * Перетворити і отримати об'єкт
     *
     * @param function функція перетворення об'єкта
     * @return LoginUserResponse відповідь API
     */
    public LoginUserResponse get(Function<UserLoginRequest, LoginUserResponse> function) {
        return function.apply(user);
    }
}
