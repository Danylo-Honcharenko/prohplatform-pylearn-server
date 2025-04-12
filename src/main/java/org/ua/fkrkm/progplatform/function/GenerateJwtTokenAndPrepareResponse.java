package org.ua.fkrkm.progplatform.function;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.ua.fkrkm.proglatformdao.dao.AuthDaoI;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.proglatformdao.entity.Auth;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.services.JwtServiceI;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static org.springframework.security.core.userdetails.User.*;

/**
 * Клас для генерації токену і підготовки відповіді для клиента
 */
public class GenerateJwtTokenAndPrepareResponse implements Function<UserLoginRequest, LoginUserResponse> {

    // Сервіс для роботи з токеном
    private final JwtServiceI jwtService;
    // DAO для роботи з користувачами
    private final UserDaoI userDao;
    // DAO для роботи з ролями
    private final RoleDaoI roleDao;
    // Назва cookie з токеном
    private final String cookiesTokenName;
    // Сервлет відповіді
    private final HttpServletResponse response;

    private final AuthDaoI authDao;

    /**
     * Конструктор
     *
     * @param jwtService сервіс для роботи з токеном
     * @param userDao DAO для роботи з користувачами
     * @param roleDao DAO для роботи з ролями
     * @param cookiesTokenName назва cookie з токеном
     * @param response відповідь
     */
    public GenerateJwtTokenAndPrepareResponse(JwtServiceI jwtService,
                                              UserDaoI userDao,
                                              RoleDaoI roleDao,
                                              String cookiesTokenName,
                                              HttpServletResponse response,
                                              AuthDaoI authDao) {
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.cookiesTokenName = cookiesTokenName;
        this.response = response;
        this.authDao = authDao;
    }

    @Override
    public LoginUserResponse apply(UserLoginRequest candidate) {
        // Отримуємо користувача
        List<User> users = userDao.findByEmail(candidate.getEmail());
        if (users.isEmpty()) throw new ProgPlatformException(ErrorConsts.USER_NOT_FOUND);
        User user = users.getFirst();
        // Отримуємо згенерований токен
        String token = this.getGeneratedJwtToken(user);
        // Встановлюємо токен в Cookie відповіді
        this.setJwtTokenToCookie(token);
        // Зберігаємо токен в базі активних токенів
        this.saveTokenInDatabase(token, user.getId());
        // Заповняємо відповідь
        return LoginUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirst_name())
                .lastName(user.getLast_name())
                .role(this.getRoleNameById(user.getRoleId()))
                .created(user.getCreated())
                .build();
    }

    /**
     * Отримуємо згенерований Jwt токен
     *
     * @param user користувач
     * @return String Jwt токен
     */
    private String getGeneratedJwtToken(User user) {
        UserBuilder buildUser = withUsername(user.getEmail());
        buildUser.password(user.getPassword());
        return jwtService.generateToken(buildUser.build());
    }

    /**
     * Встановити токен в Cookie
     *
     * @param jwtToken токен
     */
    private void setJwtTokenToCookie(String jwtToken) {
        Cookie cookie = new Cookie(cookiesTokenName, jwtToken);
        cookie.setMaxAge(36000);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    /**
     * Отримати ім'я ролі по ID
     *
     * @param id ID ролі
     * @return String ім'я ролі
     */
    private String getRoleNameById(int id) {
        try {
            // Отримуємо роль по ID
            Role role = roleDao.getById(id);
            return role.getName();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ProgPlatformException(ErrorConsts.ROLE_NOT_FOUND);
        }
    }

    /**
     * Зберегти токен в базі активних токенів
     *
     * @param token токен
     * @param userId ID користувача
     */
    private void saveTokenInDatabase(String token, int userId) {
        Long expirationTime = jwtService.getExpirationTime();
        Auth auth = Auth.builder()
                .userId(userId)
                .accessToken(token)
                .created(new Date())
                .expiresIn(new Date(System.currentTimeMillis() + expirationTime))
                .build();
        authDao.create(auth);
    }
}
