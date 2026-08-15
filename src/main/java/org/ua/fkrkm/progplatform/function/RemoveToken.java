package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.dao.AuthDaoI;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.request.UserLoginRequest;

import java.util.function.Consumer;

/**
 * Видалення старого токену з бази даних
 */
public class RemoveToken implements Consumer<UserLoginRequest> {
    // DAO для роботи з аунтифікованими користувачами
    private final AuthDaoI authDao;
    // Користувач
    private final User user;

    /**
     * Конструктор
     *
     * @param authDao DAO для роботи з аунтифікованими користувачами
     * @param user користувач
     */
    public RemoveToken(AuthDaoI authDao, User user) {
        this.authDao = authDao;
        this.user = user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(UserLoginRequest userLoginRequest) {
        this.authDao.deleteAllTokensByUserId(user.getId());
    }
}
