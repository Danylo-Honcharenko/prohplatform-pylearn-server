package org.ua.fkrkm.progplatform.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

/**
 * Інтерфейс для роботи з користувачами
 */
public interface UserServiceI {
    /**
     * Створення користувача
     *
     * @param request запит
     * @return CreateUserResponse відповідь API
     */
    CreateUserResponse registration(UserRegistrationRequest request);
    /**
     * Авторизація
     *
     * @param request запит
     * @param response відповідь
     * @return LoginUserResponse відповідь API
     */
    LoginUserResponse login(UserLoginRequest request, HttpServletResponse response);
    /**
     * Вихід із системе
     *
     * @param request запит
     * @return LogoutResponse відповідь API
     */
    LogoutResponse logout(HttpServletRequest request);
    /**
     * Отримати поточного користувача в системі
     *
     * @return CurrentUserResponse відповідь API
     */
    CurrentUserResponse getCurrentUser();
    /**
     * Оновлення даних користувача
     *
     * @param request запит
     * @return UpdateUserResponse відповідь API
     */
    UpdateUserResponse update(UpdateUserRequest request);
    /**
     * Отримання користувача по заданим параметрам
     *
     * @param id ID користувача
     * @param email Email користувача
     * @return UserResponse відповідь API
     */
    UserResponse getUserByParams(Integer id, String firstName, String lastName, String email);
    /**
     * Видалити користувача по ID
     *
     * @param id ID користувача
     * @return DeleteUserResponse відповідь API
     */
    DeleteUserResponse delete(int id);
    /**
     * Отримання всіх користувачів
     *
     * @param recordLimit кількість записів
     * @return GetAllUsersResponse відповідь API
     */
    GetAllUsersResponse getAllUsers(Integer recordLimit);
    /**
     * Оновлення ролі користувача
     *
     * @param request запит
     * @return UpdateUserRoleResponse відповідь API
     */
    UpdateUserRoleResponse updateUserRole(UpdateUserRoleRequest request);
}
