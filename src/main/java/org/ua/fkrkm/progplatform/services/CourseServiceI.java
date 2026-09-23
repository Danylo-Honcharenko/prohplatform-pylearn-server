package org.ua.fkrkm.progplatform.services;

import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

/**
 * Інтерфейс для роботи з користувачами
 */
public interface CourseServiceI {
    /**
     * Створити курс
     *
     * @param request запит
     * @return CreateCourseResponse відповідь API
     */
    CreateCourseResponse create(CreateCourseRequest request);
    /**
     * Оновити курс
     *
     * @param request запит
     * @return UpdateCourseResponse відповідь API
     */
    UpdateCourseResponse update(UpdateCourseRequest request);
    /**
     * Видалити курс
     *
     * @param id ID курсу
     * @return DeleteCourseResponse відповідь API
     */
    DeleteCourseResponse delete(Long id);
    /**
     * Отримати всі курси
     *
     * @return GetAllCoursesResponse відповідь API
     */
    GetAllCoursesResponse getAllCourses();
    /**
     * Отримати всіх користувачів курсу
     *
     * @param id ID курсу
     * @return CourseUsersResponse відповідь API
     */
    CourseUsersResponse getCourseUsers(Long id);
    /**
     * Додати користувача до курсу
     *
     * @param userId ID користувача
     * @param id ID курсу
     * @return AddUserToCourseResponse відповідь API
     */
    AddUserToCourseResponse addUserToCourse(Long userId, Long id);
    /**
     * Видалити користувача з курсу
     *
     * @param userId ID користувача
     * @param id ID курсу
     * @return DeleteUserFromCourseResponse відповідь API
     */
    DeleteUserFromCourseResponse deleteUserFromCourse(Long userId, Long id);
    /**
     * Перевіряє що користувач присутній в курсе
     *
     * @param id ID курсу
     * @param userId ID користувача
     * @return boolean true/false
     */
    boolean checkIfUserExistsInCourse(Long id, Long userId);
    /**
     * Отримати курс по ID
     *
     * @param id ID курсу
     * @return CourseResponse відповідь API
     */
    CourseResponse getCourseById(Long id);
    /**
     * Отримати всі курси користувача
     *
     * @return UserCourseResponse відповідь API
     */
    UserCourseResponse getUserCourses();
}
