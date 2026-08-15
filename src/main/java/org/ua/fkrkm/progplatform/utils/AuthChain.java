package org.ua.fkrkm.progplatform.utils;

import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformExceptionBadRequest;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Ланцюг аутентифікації
 */
public class AuthChain extends ObjectModifier<UserLoginRequest>{
    // Запит
    private final UserLoginRequest user;

    /**
     * Конструктор
     *
     * @param user запит
     */
    public AuthChain(UserLoginRequest user) {
        super(user);
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
        super.apply(action);
        return this;
    }

    /**
     * Перевірка умови
     *
     * @param predicate умова
     * @return AuthChain ланцюг аутентифікації
     */
    public AuthChain check(Predicate<UserLoginRequest> predicate) {
        boolean test = predicate.test(this.user);
        if (!test) throw new ProgPlatformExceptionBadRequest(ErrorConsts.PASSWORD_IS_INCORRECT);
        return this;
    }

    /**
     * Взяти поточний обʼєкт
     *
     * @param consumer функція яка бере обʼєкт
     * @return AuthChain ланцюг аутентифікації
     */
    public AuthChain take(Consumer<UserLoginRequest> consumer) {
        consumer.accept(this.user);
        return this;
    }

    /**
     * Перетворити і отримати об'єкт
     *
     * @param function функція перетворення об'єкта
     * @return LoginUserResponse відповідь API
     */
    public LoginUserResponse get(Function<UserLoginRequest, LoginUserResponse> function) {
        return function.apply(super.get());
    }
}