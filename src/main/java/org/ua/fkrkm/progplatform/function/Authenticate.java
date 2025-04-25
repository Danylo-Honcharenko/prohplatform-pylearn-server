package org.ua.fkrkm.progplatform.function;

import org.springframework.util.Assert;
import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformNotFoundException;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

import java.util.List;
import java.util.function.Consumer;

/**
 * Аутентифікація користувача
 */
public class Authenticate implements Consumer<UserLoginRequest> {
    // DAO для роботи з користувачами
    private final UserDaoI userDao;

    /**
     * Конструктор
     *
     * @param userDao DAO для роботи з користувачами
     */
    public Authenticate(UserDaoI userDao) {
        Assert.notNull(userDao,
                "userDao must not be null");
        this.userDao = userDao;
    }

    @Override
    public void accept(UserLoginRequest candidate) {
        Assert.notNull(candidate,
                "userLoginRequest must not be null");
        // Шукаємо користувача
        List<User> user = userDao.findByEmail(candidate.getEmail());
        // Перевіряємо що він існує
        if (user.isEmpty()) {
            throw new ProgPlatformNotFoundException(ErrorConsts.USER_NOT_FOUND);
        }
    }
}
