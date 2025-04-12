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
    DeleteCourseResponse delete(int id);
    /**
     * Отримати всі курси
     *
     * @return GetAllCoursesResponse відповідь API
     */
    GetAllCoursesResponse getAllCourses();
    /**
     * Отримати всіх користувачів курсу
     *
     * @param courseId ID курсу
     * @return CourseUsersResponse відповідь API
     */
    CourseUsersResponse getCourseUsers(int courseId);
    /**
     * Додати користувача до курсу
     *
     * @param userId ID користувача
     * @param courseId ID курсу
     * @return AddUserToCourseResponse відповідь API
     */
    AddUserToCourseResponse addUserToCourse(int userId, int courseId);
    /**
     * Видалити користувача з курсу
     *
     * @param userId ID користувача
     * @param courseId ID курсу
     * @return DeleteUserFromCourseResponse відповідь API
     */
    DeleteUserFromCourseResponse deleteUserFromCourse(int userId, int courseId);
    /**
     * Перевіряє що користувач присутній в курсе
     *
     * @param courseId ID курсу
     * @param userId ID користувача
     * @return boolean true/false
     */
    boolean checkIfUserExistsInCourse(int courseId, int userId);
    /**
     * Отримати курс по ID
     *
     * @param courseId ID курсу
     * @param userId ID користувача
     * @return CourseResponse відповідь API
     */
    CourseResponse getCourseById(int courseId, Integer userId);
    /**
     * Отримати всі курси користувача
     *
     * @param userId ID користувача
     * @return UserCourseResponse відповідь API
     */
    UserCourseResponse getCourseByUserId(int userId);
}
