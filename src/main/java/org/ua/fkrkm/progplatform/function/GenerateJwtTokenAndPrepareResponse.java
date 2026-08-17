package org.ua.fkrkm.progplatform.function;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.ua.fkrkm.proglatformdao.dao.AuthDaoI;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.entity.Auth;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatform.consts.Roles;
import org.ua.fkrkm.progplatform.dto.GeneratedToken;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformNotFoundException;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.JwtServiceI;

import java.util.Date;
import java.util.function.Function;

/**
 * Клас для генерації токену і підготовки відповіді для клиента
 */
public class GenerateJwtTokenAndPrepareResponse implements Function<UserLoginRequest, LoginUserResponse> {

    // Сервіс для роботи з токеном
    private final JwtServiceI jwtService;
    // Користувач
    private final User user;
    // DAO для роботи з ролями
    private final RoleDaoI roleDao;
    // Назва cookie з токеном
    private final String cookiesTokenName;
    // Сервлет відповіді
    private final HttpServletResponse response;
    // DAO для роботи з аунтифікованими користувачами
    private final AuthDaoI authDao;

    /**
     * Конструктор
     *
     * @param jwtService сервіс для роботи з токеном
     * @param user користувач
     * @param roleDao DAO для роботи з ролями
     * @param cookiesTokenName назва cookie з токеном
     * @param authDao DAO для роботи з аунтифікованими користувачами
     * @param response відповідь
     */
    public GenerateJwtTokenAndPrepareResponse(JwtServiceI jwtService,
                                              User user,
                                              RoleDaoI roleDao,
                                              String cookiesTokenName,
                                              HttpServletResponse response,
                                              AuthDaoI authDao) {
        this.jwtService = jwtService;
        this.user = user;
        this.roleDao = roleDao;
        this.cookiesTokenName = cookiesTokenName;
        this.response = response;
        this.authDao = authDao;
    }

    @Override
    public LoginUserResponse apply(UserLoginRequest candidate) {
        // Отримуємо інформацію по згенерованому токену
        GeneratedToken tokenInfo = this.getGeneratedJwtTokenInfo(user);
        // Згенерований токен
        String token = tokenInfo.getToken();
        // Час коли токен затухне
        Date tokenExpirationDate = tokenInfo.getExpired();
        // Зберігаємо токен в базі активних токенів
        this.saveTokenInDatabase(token, user.getId(), tokenExpirationDate);
        // Встановлюємо токен в Cookie відповіді
        this.setJwtTokenToCookie(token);
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
     * @return Map<String, String> інформація згенерованому токену
     */
    private GeneratedToken getGeneratedJwtTokenInfo(User user) {
        return jwtService.generateToken(user.getEmail());
    }

    /**
     * Встановити токен в Cookie
     *
     * @param jwtToken токен
     */
    private void setJwtTokenToCookie(String jwtToken) {
        ResponseCookie cookie = ResponseCookie
                .from(cookiesTokenName, jwtToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(360)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * Отримати ім'я ролі по ID
     *
     * @param id ID ролі
     * @return String ім'я ролі
     */
    private String getRoleNameById(int id) {
        String name = Roles.getNameById((long) id);

        if (StringUtils.isEmpty(name)) {
            return this.roleDao.getById(id).stream()
                    .findFirst()
                    .orElseThrow(() -> new ProgPlatformNotFoundException(ErrorConsts.ROLE_NOT_FOUND))
                    .getName();
        }

        return name;
    }

    /**
     * Зберегти токен в базі активних токенів
     *
     * @param token токен
     * @param userId ID користувача
     */
    private void saveTokenInDatabase(String token, int userId, Date tokenExpirationDate) {
        Auth auth = Auth.builder()
                .userId(userId)
                .accessToken(token)
                .created(new Date())
                .expiresIn(tokenExpirationDate)
                .build();
        authDao.create(auth);
    }
}
