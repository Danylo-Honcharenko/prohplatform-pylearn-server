package org.ua.fkrkm.progplatform.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.ua.fkrkm.progplatform.services.impl.UserServiceImpl;
import org.ua.fkrkm.progplatformclientlib.request.UpdateUserRequest;
import org.ua.fkrkm.progplatformclientlib.request.UserLoginRequest;
import org.ua.fkrkm.progplatformclientlib.request.UserRegistrationRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserControllerTest {

    private UserController userController;
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        this.userService = mock(UserServiceImpl.class);
        this.userController = new UserController(userService);
    }

    @Test
    public void registrationTest() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        userController.registration(request);
        verify(userService).registration(request);
    }

    @Test
    public void loginTest() {
        UserLoginRequest request = new UserLoginRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        userController.login(request, response);
        verify(userService).login(request, response);
    }

    @Test
    public void logoutTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        userController.logout(request, response);
        verify(userService).logout(request, response);
    }

    @Test
    public void updateTest() {
        UpdateUserRequest request = new UpdateUserRequest();
        userController.update(request);
        verify(userService).update(request);
    }

    @Test
    public void getCurrentUserTest() {
        userController.getCurrentUser();
        verify(userService).getCurrentUser();
    }

    @Test
    public void getUserByParamsTest() {
        userController.getUserByParams("firstName", "lastName", "email");
        verify(userService).getUserByParams("firstName", "lastName", "email");
    }

    @Test
    public void deleteTest() {
        userController.delete(1L);
        verify(userService).delete(1L);
    }
}
