package org.ua.fkrkm.progplatform.utils;

import org.ua.fkrkm.progplatform.exceptions.ErrorCfg;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformExceptionBadRequest;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Ланцюг аутентифікації
 *
 * @param <T> тип об'єкта
 */
public class AuthChain<T> {
    // Запит
    private final T request;

    /**
     * Конструктор
     *
     * @param request запит
     */
    private AuthChain(T request) {
        this.request = request;
    }

    /**
     * Метод ініціалізації
     *
     * @param request запит
     * @param <T> типа об'єкта
     * @return AuthChain<T> ланцюг аутентифікації
     */
    public static <T> AuthChain<T> init(T request) {
        return new AuthChain<>(request);
    }

    /**
     * Застосувати зміни до об'єкта
     *
     * @param action дія
     * @return AuthChain<T> ланцюг аутентифікації
     */
    public AuthChain<T> apply(Consumer<T> action) {
        action.accept(request);
        return this;
    }

    /**
     * Перевірка умови
     *
     * @param predicate умова
     * @return AuthChain<T> ланцюг аутентифікації
     */
    public AuthChain<T> check(Predicate<T> predicate, ErrorCfg error) {
        boolean test = predicate.test(request);
        if (!test) throw new ProgPlatformExceptionBadRequest(error);
        return this;
    }

    /**
     * Перетворити і отримати об'єкт
     *
     * @param function функція перетворення об'єкта
     * @return LoginUserResponse відповідь API
     */
    public LoginUserResponse get(Function<T, LoginUserResponse> function) {
        return function.apply(request);
    }
}
