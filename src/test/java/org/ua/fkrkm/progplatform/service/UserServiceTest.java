package org.ua.fkrkm.progplatform.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.proglatformdao.dao.impl.AuthDaoImpl;
import org.ua.fkrkm.proglatformdao.dao.impl.RoleDaoImpl;
import org.ua.fkrkm.proglatformdao.dao.impl.UserDaoImpl;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatform.converters.*;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;
import org.ua.fkrkm.progplatform.services.JwtServiceI;
import org.ua.fkrkm.progplatform.services.UserServiceI;
import org.ua.fkrkm.progplatform.services.impl.AuthUserServiceImpl;
import org.ua.fkrkm.progplatform.services.impl.JwtServiceImpl;
import org.ua.fkrkm.progplatform.services.impl.UserServiceImpl;
import org.ua.fkrkm.progplatformclientlib.request.UserLoginRequest;
import org.ua.fkrkm.progplatformclientlib.request.UserRegistrationRequest;
import org.ua.fkrkm.progplatformclientlib.response.CreateUserResponse;
import org.ua.fkrkm.progplatformclientlib.response.CurrentUserResponse;
import org.ua.fkrkm.progplatformclientlib.response.LogoutResponse;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private UserServiceI userService;
    private final UserDaoI userDao = mock(UserDaoImpl.class);
    private final JwtServiceI jwtService = mock(JwtServiceImpl.class);
    private final RoleDaoI roleDao = mock(RoleDaoImpl.class);
    private final Converter<UserRegistrationRequest, User> userRegistrationRequestUserConverter = mock(UserRequestToUser.class);
    private final Converter<User, CreateUserResponse> createUserResponseConverter = mock(UserToCreateUserResponse.class);
    private final AuthUserServiceI authUserService = mock(AuthUserServiceImpl.class);
    private final Converter<User, CurrentUserResponse> currentUserResponseConverter = mock(UserToCurrentUserResponse.class);

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.userService = new UserServiceImpl(userDao, roleDao, userRegistrationRequestUserConverter,
                createUserResponseConverter, jwtService, passwordEncoder, authUserService,
                mock(UserToUpdateUserResponse.class), currentUserResponseConverter, mock(UserToUserViewExt.class), mock(AuthDaoImpl.class));

        Field cookiesTokenName = UserServiceImpl.class.getDeclaredField("cookiesTokenName");
        cookiesTokenName.setAccessible(true);
        cookiesTokenName.set(userService, "accessToken");
    }

    @Test
    public void registrationTest() {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("email")
                .build();

        when(userRegistrationRequestUserConverter.convert(any(UserRegistrationRequest.class)))
                .thenReturn(User.builder().build());

        when(userDao.create(any(User.class)))
                .thenReturn(1);

        when(createUserResponseConverter.convert(any(User.class)))
                .thenReturn(CreateUserResponse.builder().build());

        assertNotNull(userService.registration(request));
    }

    @Test
    public void loginTest() {
        User user = User.builder()
                .id(1)
                .first_name("John")
                .last_name("Smith")
                .email("test.test@gmail.com")
                .roleId(1)
                .password(passwordEncoder.encode("password"))
                .created(new Date())
                .build();

        Role role = Role.builder()
                .id(1)
                .name("ROLE")
                .build();

        when(userDao.findByEmail(anyString()))
                .thenReturn(List.of(user));

        when(jwtService.generateToken(any()))
                .thenReturn("token");

        when(roleDao.getById(anyInt()))
                .thenReturn(role);

        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();

        assertNotNull(userService.login(userLoginRequest, mock(HttpServletResponse.class)));
    }

    @Test
    public void logoutTest() {
        Cookie cookie = new Cookie("accessToken", "123456789");
        Cookie[] cookies = new Cookie[]{cookie};

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getCookies())
                .thenReturn(cookies);

        LogoutResponse logoutResponse = userService.logout(request);

        assertEquals("LOGOUT", logoutResponse.getStatus());
    }

    @Test
    public void getCurrentUserTest() {
        when(authUserService.getCurrentAuthUser())
                .thenReturn(User.builder().build());

        when(currentUserResponseConverter.convert(any(User.class)))
                .thenReturn(CurrentUserResponse.builder().build());

        assertNotNull(userService.getCurrentUser());
    }
}
