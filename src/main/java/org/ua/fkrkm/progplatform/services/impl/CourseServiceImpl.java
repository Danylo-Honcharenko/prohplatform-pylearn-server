package org.ua.fkrkm.progplatform.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.*;
import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.proglatformdao.entity.view.UserView;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformNotFoundException;
import org.ua.fkrkm.progplatform.function.*;
import org.ua.fkrkm.progplatform.utils.ObjectModifier;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.converters.CourseToCourseResponse;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;
import org.ua.fkrkm.progplatform.services.CourseServiceI;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    // Конвертор
    private final CourseToCourseResponse courseResponseCourseConverter;
    // DAO для роботи з модулями
    private final ModuleDaoI moduleDao;
    // DAO для роботи з темами
    private final TopicDaoI topicDao;
    // DAO для роботи зі статистикой по модулю
    private final ModuleStatDaoI moduleStatDao;
    // DAO для роботи з тестами
    private final TestDaoI testDao;

    /**
     * {@inheritDoc}
     */
    @Override
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
        // Перевіряємо що поточний користувач є в списку користувачів курсу
        if (!userExistsInCourse && !authUserService.isCurrentAuthUserAdmin())
            throw new ProgPlatformException(String.format("Користувач з ID: %s не є участником цього курсу!", userId));
        // Отримуємо курс по ID
        Course course = courseDao.getById(request.getId());

        // Заповнюємо оновлені дані якщо вони є
        Optional.ofNullable(request.getName()).filter(s -> !s.isBlank()).ifPresent(course::setName);
        Optional.ofNullable(request.getDescription()).filter(s -> !s.isBlank()).ifPresent(course::setDescription);

        course.setUpdated(new Date());
        // Оновлюємо запис в базі
        courseDao.update(course);
        return new UpdateCourseResponse(course.getId(), course.getName(), course.getDescription(), course.getUpdated());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteCourseResponse delete(int id) {
        try {
            // Перевіряємо що курс існує
            courseDao.getById(id);
            // Видаляємо сам курс
            courseDao.delete(id);
            return new DeleteCourseResponse(id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ProgPlatformNotFoundException(ErrorConsts.COURSE_NOT_FOUND);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAllCoursesResponse getAllCourses() {
        List<CourseResponse> courses = courseDao.getAll().stream()
                .map(courseResponseCourseConverter::convert)
                .toList();
        return new GetAllCoursesResponse(courses);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseUsersResponse getCourseUsers(int courseId) {
        // Отримуємо поточного користувача в системі
        User currentAuthUser = authUserService.getCurrentAuthUser();
        // Отримуємо ID користувача
        Integer userId = currentAuthUser.getId();
        boolean userExistsInCourse = checkIfUserExistsInCourse(courseId, userId);
        // Перевіряємо що поточний користувач є в цьому списку
        if (!userExistsInCourse && !authUserService.isCurrentAuthUserAdmin())
            throw new ProgPlatformException(String.format("Користувач з ID: %s не є участником цього курсу!", userId));

        // Отримуємо курс по ID
        Course course = courseDao.getById(courseId);
        List<Integer> courseUsersId = courseDao.getCourseUsersIdByCourseId(courseId);
        // Формуємо список користувачів
        List<UserView> users = courseUsersId.stream()
                // По ID користувача отримуємо інформацію з бази та створюємо список
                .map(new GetUserInfo(userDao))
                .toList();
        return new CourseUsersResponse(courseId, course.getName(), users);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddUserToCourseResponse addUserToCourse(int userId, int courseId) {
        // Отримуємо поточного користувача в системі
        User currentAuthUser = authUserService.getCurrentAuthUser();
        Integer userCreatorId = currentAuthUser.getId();
        boolean userExistsInCourse = checkIfUserExistsInCourse(courseId, userId);
        // Перевіряємо що поточний користувач є в цьому списку
        if (!userExistsInCourse && !authUserService.isCurrentAuthUserAdmin())
            throw new ProgPlatformException("Користувач з ID: " + userCreatorId + " не є участником цього курсу!");
        // Додаємо користувача до курсу
        courseDao.addUserToCourse(courseId, userId);
        return new AddUserToCourseResponse(courseId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteUserFromCourseResponse deleteUserFromCourse(int userId, int courseId) {
        boolean userExistsInCourse = checkIfUserExistsInCourse(courseId, userId);
        // Перевіряємо що поточний користувач є в цьому списку
        if (!userExistsInCourse)
            throw new ProgPlatformException(String.format("Користувач з ID: %s не є участником цього курсу!", userId));
        // Видаляємо користувача з курсу
        courseDao.removeUserFromCourse(courseId, userId);
        return new DeleteUserFromCourseResponse(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfUserExistsInCourse(int courseId, int userId) {
        try {
            // Отримуємо курс по ID
            Course course = courseDao.getById(courseId);
            // Отримуємо список ID користувачів по ID курсу
            List<Integer> courseUsersId = courseDao.getCourseUsersIdByCourseId(course.getId());
            return courseUsersId.contains(userId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ProgPlatformNotFoundException(ErrorConsts.COURSE_NOT_FOUND);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseResponse getCourseById(int courseId, Integer userId) {
        try {
            // Заповнюємо объект
            return ObjectModifier.init(new CourseResponse())
                    // Отримуємо курс по ID та заповнюємо объект
                    .apply(new SetCourse(() -> this.courseDao.getById(courseId)))
                    // Встановлюємо модулі по ID курсу
                    .apply(new SetModuleByCourseId(() -> this.moduleDao.getModulesByCourseId(courseId)))
                    // Встановлюємо теми модулів
                    .apply(new SetModuleTopic(this.topicDao::findAllTopicsByModuleIdList,
                            () -> this.moduleStatDao.findModuleStatByUserId(userId)))
                    // Встановлюємо тест
                    .apply(new SetTopicTest(this.testDao::getByTopicIds))
                    // Встановлюємо процент проходження модулів
                    .apply(new SetModulePercent(() -> this.moduleStatDao.findModulesStatByUserId(userId)))
                    // Отримуємо объект
                    .get();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ProgPlatformNotFoundException(ErrorConsts.COURSE_NOT_FOUND);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserCourseResponse getCourseByUserId(int userId) {
        List<Course> courses = courseDao.getCoursesIdByUserId(userId);
        return new UserCourseResponse(courses);
    }
}
