package org.ua.fkrkm.progplatform.services.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.AuthDaoI;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.proglatformdao.entity.view.UserView;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformExceptionBadRequest;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformNotFoundException;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;
import org.ua.fkrkm.progplatform.services.JwtServiceI;
import org.ua.fkrkm.progplatform.services.UserServiceI;
import org.ua.fkrkm.progplatform.function.GenerateJwtTokenAndPrepareResponse;
import org.ua.fkrkm.progplatform.function.Authenticate;
import org.ua.fkrkm.progplatform.function.ValidatePasswordHash;
import org.ua.fkrkm.progplatform.utils.AuthChain;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * Сервіс для роботи з користувачами
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceI {
    // DAO для роботи з користувачами
    private final UserDaoI userDao;
    // DAO для роботи з ролями
    private final RoleDaoI roleDao;
    // Конвертор
    private final Converter<UserRegistrationRequest, User> createUserRequestConverter;
    // Конвертор
    private final Converter<User, CreateUserResponse> createUserResponseConverter;
    // Сервіс для роботи з токеном
    private final JwtServiceI jwtService;
    // Інтерфейс для створення захешованого паролю
    private final PasswordEncoder passwordEncoder;
    // Сервіс для роботи з поточним користувачем в системі
    private final AuthUserServiceI authUserService;
    // Конвертор
    private final Converter<User, UpdateUserResponse> updateUserResponseConverter;
    // Конвертор
    private final Converter<User, CurrentUserResponse> currentUserResponseConverter;
    // Конвертор
    private final Converter<User, UserView> userViewExtConverter;
    // DAO для роботи з аунтифікованими користувачами
    private final AuthDaoI authDao;
    // Ім'я кукі з JWT токеном
    @Value("${cookies.jwt.token.name}")
    private String cookiesTokenName;
    // Поточний домен
    @Value("${my.server.domain}")
    private String domain;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateUserResponse registration(UserRegistrationRequest request) {
        User user = createUserRequestConverter.convert(request);
        int id = userDao.create(user);
        user.setId(id);
        return createUserResponseConverter.convert(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginUserResponse login(UserLoginRequest request, HttpServletResponse response) {
        return AuthChain.init(request)
                // Аутентифікуємо користувача
                .apply(new Authenticate(userDao))
                // Перевіряємо хеш пароля
                .check(new ValidatePasswordHash(passwordEncoder, userDao), ErrorConsts.PASSWORD_IS_INCORRECT)
                // Генеруємо Jwt токен і готуємо відповідь для клієнта
                .get(new GenerateJwtTokenAndPrepareResponse(jwtService, userDao, roleDao, cookiesTokenName, response, authDao, domain));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogoutResponse logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        // Перевіряємо що cookie встановлені
        if (cookies == null) return new LogoutResponse("TOKEN NO CONTAINED IN COOKIES!");
        Optional<Cookie> optionalCookie = Stream.of(cookies)
                // Намагаємось знайти cookie за ім'ям
                .filter(cookie -> cookie.getName().equals(cookiesTokenName))
                .findFirst();
        boolean isCookiePresent = optionalCookie.isPresent();
        // Перевіряємо що потрібний cookie встановлено
        if (isCookiePresent) {
            Cookie cookie = optionalCookie.get();
            String token = cookie.getValue();
            // Видаляємо токен з бази
            authDao.deleteByAccessToken(token);
        }
        return new LogoutResponse(isCookiePresent ? "LOGOUT" : "CANNOT LOGOUT!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CurrentUserResponse getCurrentUser() {
        // Отримуємо сутність поточного користувача в системі
        User currentAuthUser = authUserService.getCurrentAuthUser();
        return currentUserResponseConverter.convert(currentAuthUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateUserResponse update(UpdateUserRequest request) {
        // Отримуємо сутність поточного користувача в системі
        User user = authUserService.getCurrentAuthUser();

        // Заповнюємо сутність оновленими даними, якщо вони присутні
        Optional.ofNullable(request.getFirstName()).filter(e -> !e.isBlank()).ifPresent(user::setFirst_name);
        Optional.ofNullable(request.getLastName()).filter(e -> !e.isBlank()).ifPresent(user::setLast_name);
        Optional.ofNullable(request.getEmail()).filter(e -> !e.isBlank()).ifPresent(user::setEmail);

        user.setUpdated(new Date());
        // Оновлюємо користувача в базі
        userDao.update(user);
        return updateUserResponseConverter.convert(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        // Намагаємось отримати користувача по email
        List<User> users = userDao.findByEmail(request.getEmail());
        // Перевіряємо, що такий користувач є
        if (users.isEmpty()) throw new ProgPlatformNotFoundException(ErrorConsts.USER_NOT_FOUND);
        User user = users.getFirst();
        // Перевіряємо переданий пароль з паролем у базі
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) throw new ProgPlatformExceptionBadRequest(ErrorConsts.PASSWORD_IS_INCORRECT);
        // Хешуємо та зберігаємо новий пароль
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Оновлюємо сутність у базі
        userDao.update(user);
        return new ChangePasswordResponse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponse getUserByParams(Integer id, String firstName, String lastName, String email) {
        // Отримуємо користувача по параметрам
        List<User> users = userDao.findByParams(id, firstName, lastName, email);
        // Перевіряємо що користувач існує
        if (users.isEmpty()) throw new ProgPlatformNotFoundException(ErrorConsts.USER_NOT_FOUND);
        User user = users.getFirst();
        Role role = roleDao.getById(user.getRoleId());
        return new UserResponse(user.getId(), user.getFirst_name(), user.getLast_name(), user.getEmail(), role.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteUserResponse delete(int id) {
        // Отримуємо розширену сутність поточного користувача в системі
        User currentAuthUser = authUserService.getCurrentAuthUser();
        try {
            // Отримуємо користувача по ID
            User user = userDao.getById(id);
            // Перевіряємо що користувач не намагається видалити іншого користувача крім себе
            if (!user.getEmail().equals(currentAuthUser.getEmail()))
                throw new ProgPlatformException(ErrorConsts.CANNOT_DELETE_ANOTHER_USER);

            userDao.delete(id);
            return new DeleteUserResponse(id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ProgPlatformNotFoundException(ErrorConsts.USER_NOT_FOUND);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAllUsersResponse getAllUsers(Integer recordLimit) {
        List<UserView> users = userDao.findAll(ObjectUtils.defaultIfNull(recordLimit, 0)).stream()
                .map(userViewExtConverter::convert)
                .toList();
        return new GetAllUsersResponse(users);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateUserRoleResponse updateUserRole(UpdateUserRoleRequest request) {
        // Перевіряємо що користувач який міняє роль адмін
        if (!authUserService.isCurrentAuthUserAdmin()) throw new AccessDeniedException("Ви маєте бути адміном!");
        // Отримуємо користувача якому ми змінюємо роль
        User user = userDao.getById(request.getUserId());
        // Отримуємо роль на яку будемо міняти
        Integer roleId = roleDao.findIdByName(request.getRoleName())
                .getFirst();
        // Перевіряємо що роль існує
        if (roleId == null) throw new ProgPlatformNotFoundException(ErrorConsts.ROLE_NOT_FOUND);
        Role newRole = roleDao.getById(roleId);
        Role oldRole = roleDao.getById(user.getRoleId());
        // Встановлюємо ID нової ролі
        user.setRoleId(roleId);
        userDao.update(user);
        return new UpdateUserRoleResponse(user.getId(), user.getEmail(), newRole.getName(), oldRole.getName());
    }
}
