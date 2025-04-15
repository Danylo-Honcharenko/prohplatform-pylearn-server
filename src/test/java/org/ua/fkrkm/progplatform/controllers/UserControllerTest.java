package org.ua.fkrkm.progplatform.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.ua.fkrkm.progplatform.services.impl.UserServiceImpl;
import org.ua.fkrkm.progplatformclientlib.request.UpdateUserRequest;
import org.ua.fkrkm.progplatformclientlib.request.UserLoginRequest;
import org.ua.fkrkm.progplatformclientlib.request.UserRegistrationRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    public void setUp() {
        this.userController = new UserController(mock(UserServiceImpl.class));
    }

    @Test
    public void registrationTest() {
        assertNotNull(userController.registration(new UserRegistrationRequest()));
    }

    @Test
    public void loginTest() {
        assertNotNull(userController.login(new UserLoginRequest(), new MockHttpServletResponse()));
    }

    @Test
    public void logoutTest() {
        assertNotNull(userController.logout(new MockHttpServletRequest()));
    }

    @Test
    public void updateTest() {
        assertNotNull(userController.update(new UpdateUserRequest()));
    }

    @Test
    public void getCurrentUserTest() {
        assertNotNull(userController.getCurrentUser());
    }

    @Test
    public void getUserByParamsTest() {
        assertNotNull(userController.getUserByParams(1, "firstName", "lastName", "email"));
    }

    @Test
    public void deleteTest() {
        assertNotNull(userController.delete(1));
    }
}
