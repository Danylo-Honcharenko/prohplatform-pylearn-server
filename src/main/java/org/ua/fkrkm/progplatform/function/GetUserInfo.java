package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.proglatformdao.entity.view.UserView;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

import java.util.List;
import java.util.function.Function;

/**
 * Отримання інформація по користувачу використовуючи його ID
 */
public class GetUserInfo implements Function<Integer, UserView> {

    // DAO для роботи з користувачами
    private final UserDaoI userDao;

    /**
     * Конструктор
     *
     * @param userDao DAO для роботи з користувачами
     */
    public GetUserInfo(UserDaoI userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserView apply(Integer id) {
        List<User> users = userDao.getById(id);
        if (users.isEmpty()) throw new ProgPlatformException(ErrorConsts.USER_NOT_FOUND);
        User user = users.getFirst();
        return UserView.builder()
                .id(user.getId())
                .firstName(user.getFirst_name())
                .lastName(user.getLast_name())
                .email(user.getEmail())
                .build();
    }
}
