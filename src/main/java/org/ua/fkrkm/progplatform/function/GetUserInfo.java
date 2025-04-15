package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.proglatformdao.entity.view.UserView;

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
        User user = userDao.getById(id);
        return UserView.builder()
                .id(user.getId())
                .firstName(user.getFirst_name())
                .lastName(user.getLast_name())
                .email(user.getEmail())
                .build();
    }
}
