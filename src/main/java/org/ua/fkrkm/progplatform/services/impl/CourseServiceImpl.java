package org.ua.fkrkm.progplatform.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ua.fkrkm.proglatformdao.dao.*;
import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.proglatformdao.entity.view.UserView;
import org.ua.fkrkm.progplatform.converters.MultiConverter;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformNotFoundException;
import org.ua.fkrkm.progplatform.function.*;
import org.ua.fkrkm.progplatform.utils.ObjectModifier;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;
import org.ua.fkrkm.progplatform.services.CourseServiceI;

import java.util.*;

/**
 * Сервіс для роботи з курсами
 */
@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseServiceI {
    // DAO для роботи з курсами
    private final CourseDaoI courseDao;
    // DAO для роботи з користувачами
    private final UserDaoI userDao;
    // Конвертор
    private final Converter<CreateCourseRequest, Course> createCourseRequestCourseConverter;
    // Конвертор
    private final Converter<Course, CreateCourseResponse> createCourseResponseCourseConverter;
    // Сервіс для роботи з поточним користувачем в системі
    private final AuthUserServiceI authUserService;
    // DAO для роботи зі статистикой по модулю
    private final ModuleStatDaoI moduleStatDao;
    // Конвертор
    private final MultiConverter<Course, CourseResponse> courseToCourseResponseConverter;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CreateCourseResponse create(CreateCourseRequest request) {
        // Отримуємо поточного користувача в системі
        User currentAuthUser = authUserService.getCurrentAuthUser();
        Course course = createCourseRequestCourseConverter.convert(request);
        // Створюємо курс в базі
        int id = courseDao.create(course);
        course.setId(id);
        // Додаємо того хто створював курс в цей же курс
        courseDao.addUserToCourse(id, currentAuthUser.getId());
        return createCourseResponseCourseConverter.convert(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateCourseResponse update(UpdateCourseRequest request) {
        // Отримуємо поточного користувача в системі
        User currentAuthUser = authUserService.getCurrentAuthUser();
        // Отримуємо ID користувача
        Integer userId = currentAuthUser.getId();
        boolean userExistsInCourse = checkIfUserExistsInCourse(request.getId(), userId);
        // Перевіряємо, що поточний користувач є в списку користувачів курсу
        if (!userExistsInCourse && !authUserService.isCurrentAuthUserAdmin())
            throw new ProgPlatformException(ErrorConsts.INSUFFICIENT_RIGHTS);
        // Отримуємо курс по ID
        List<Course> courses = courseDao.getById(request.getId());
        if (courses.isEmpty()) throw new ProgPlatformNotFoundException(ErrorConsts.COURSE_NOT_FOUND);
        Course course = courses.getFirst();

        // Заповнюємо оновлені дані, якщо вони є
        Optional.ofNullable(request.getName()).filter(s -> !s.isBlank()).ifPresent(course::setName);
        Optional.ofNullable(request.getDescription()).filter(s -> !s.isBlank()).ifPresent(course::setDescription);

        course.setUpdated(new Date());
        // Оновлюємо запис у базі
        courseDao.update(course);
        return new UpdateCourseResponse(course.getId(), course.getName(), course.getDescription(), course.getUpdated());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteCourseResponse delete(int id) {
        // Перевіряємо, що курс існує
        List<Course> courses = courseDao.getById(id);
        if (courses.isEmpty()) throw new ProgPlatformNotFoundException(ErrorConsts.COURSE_NOT_FOUND);
        // Видаляємо сам курс
        courseDao.delete(id);
        return new DeleteCourseResponse(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAllCoursesResponse getAllCourses() {
        List<Course> courses = courseDao.getAll();
        List<CourseResponse> courseResponses = this.courseToCourseResponseConverter.convert(courses);
        return new GetAllCoursesResponse(courseResponses);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseUsersResponse getCourseUsers(int id) {
        // Отримуємо поточного користувача в системі
        User currentAuthUser = authUserService.getCurrentAuthUser();
        // Отримуємо ID користувача
        Integer userId = currentAuthUser.getId();
        boolean userExistsInCourse = this.checkIfUserExistsInCourse(id, userId);
        // Перевіряємо, що поточний користувач є в цьому списку
        if (!userExistsInCourse && !authUserService.isCurrentAuthUserAdmin())
            throw new ProgPlatformException(ErrorConsts.INSUFFICIENT_RIGHTS);

        // Отримуємо курс по ID
        List<Course> courses = courseDao.getById(id);
        if (courses.isEmpty()) throw new ProgPlatformNotFoundException(ErrorConsts.COURSE_NOT_FOUND);
        Course course = courses.getFirst();

        List<Integer> courseUsersId = courseDao.getCourseUsersIdByCourseId(id);
        // Формуємо список користувачів
        List<UserView> users = courseUsersId.stream()
                // По ID користувача отримуємо інформацію з бази та створюємо список
                .map(new GetUserInfo(userDao))
                .toList();
        return new CourseUsersResponse(id, course.getName(), users);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddUserToCourseResponse addUserToCourse(int userId, int id) {
        boolean userExistsInCourse = this.checkIfUserExistsInCourse(id, userId);
        // Перевіряємо, що поточний користувач є в цьому списку
        if (!userExistsInCourse && !authUserService.isCurrentAuthUserAdmin())
            throw new ProgPlatformException(ErrorConsts.INSUFFICIENT_RIGHTS);
        // Додаємо користувача до курсу
        courseDao.addUserToCourse(id, userId);
        return new AddUserToCourseResponse(id, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteUserFromCourseResponse deleteUserFromCourse(int userId, int id) {
        boolean userExistsInCourse = this.checkIfUserExistsInCourse(id, userId);
        // Перевіряємо, що поточний користувач є в цьому списку
        if (!userExistsInCourse)
            throw new ProgPlatformException(ErrorConsts.INSUFFICIENT_RIGHTS);
        // Видаляємо користувача з курсу
        courseDao.removeUserFromCourse(id, userId);
        return new DeleteUserFromCourseResponse(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfUserExistsInCourse(int id, int userId) {
        // Отримуємо курс по ID
        List<Course> courses = courseDao.getById(id);
        if (courses.isEmpty()) throw new ProgPlatformNotFoundException(ErrorConsts.COURSE_NOT_FOUND);
        Course course = courses.getFirst();
        // Отримуємо список ID користувачів по ID курсу
        List<Integer> courseUsersId = courseDao.getCourseUsersIdByCourseId(course.getId());
        return courseUsersId.contains(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseResponse getCourseById(int id) {
        // Отримуємо курс по ID
        Course course = this.courseDao.getById(id).stream()
                .findFirst()
                .orElseThrow(() -> new ProgPlatformNotFoundException(ErrorConsts.COURSE_NOT_FOUND));

        return this.courseToCourseResponseConverter.convert(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseResponse getCourseWithPassingStatistics(int id) {
        User authUser = authUserService.getCurrentAuthUser();
        CourseResponse course = this.getCourseById(id);
        // Заповнюємо обʼєкт
        return ObjectModifier.init(course)
                // Встановлює статус перегляду теми
                .apply(new SetTopicViewingStatus(() -> this.moduleStatDao.findModuleStatByUserId(authUser.getId())))
                // Встановлюємо процент проходження модулів
                .apply(new SetModulePercent(() -> this.moduleStatDao.findModulesStatByUserId(authUser.getId())))
                // Отримуємо объект
                .get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserCourseResponse getUserCourses() {
        User authUser = authUserService.getCurrentAuthUser();
        // Отримуємо всі курси користувача
        List<Course> courses = courseDao.getCoursesIdByUserId(authUser.getId());

        List<CourseResponse> courseResponses = this.courseToCourseResponseConverter.convert(courses);

        return new UserCourseResponse(courseResponses);
    }
}
