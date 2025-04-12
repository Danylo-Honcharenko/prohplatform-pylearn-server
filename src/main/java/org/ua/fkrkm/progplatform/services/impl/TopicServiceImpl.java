package org.ua.fkrkm.progplatform.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.ModuleDaoI;
import org.ua.fkrkm.proglatformdao.dao.TopicDaoI;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;
import org.ua.fkrkm.progplatform.services.CourseServiceI;
import org.ua.fkrkm.progplatform.services.TopicServiceI;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Сервіс для роботи з темами
 */
@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicServiceI {

    // DAO для роботи з модулями
    private final ModuleDaoI moduleDao;
    // Сервіс для роботи з курсами
    private final CourseServiceI courseService;
    // DAO для роботи з темами
    private final TopicDaoI topicDao;
    // Сервіс для роботи з поточним користувачем в системі
    private final AuthUserServiceI authUserService;
    // Конвертор
    private final Converter<CreateTopicRequest, Topic> createTopicRequestTopicConverter;
    // Конвертор
    private final Converter<Topic, CreateTopicResponse> createTopicResponseTopicConverter;
    // Конвертор
    private final Converter<Topic, UpdateTopicResponse> topicToUpdateTopicResponseConverter;
    // Конвертор
    private final Converter<Topic, TopicResponse> topicResponseTopicConverter;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateTopicResponse create(CreateTopicRequest request) {
        Topic topic = createTopicRequestTopicConverter.convert(request);
        // Створюємо тему
        int id = topicDao.create(topic);
        topic.setId(id);
        return createTopicResponseTopicConverter.convert(topic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateTopicResponse update(UpdateTopicRequest request) {
        // Отримуємо поточного користувача в системі
        User currentAuthUser = authUserService.getCurrentAuthUser();
        // Отримуємо ID користувача
        Integer userId = currentAuthUser.getId();
        Integer courseId = request.getCourseId();
        try {
            if (courseId != null) {
                boolean userExistsInCourse = courseService.checkIfUserExistsInCourse(courseId, userId);
                // Перевіряємо що поточний користувач є в цьому списку
                if (userExistsInCourse && !authUserService.isCurrentAuthUserAdmin())
                    throw new ProgPlatformException("Користувач з ID: " + userId + " не є участником курсу з ID " + courseId + "!");
            }
            // Отримуємо тему по ID
            Topic topic = topicDao.getById(request.getId());

            // Заповнюємо оновлені дані якщо вони є
            Optional.ofNullable(request.getName()).filter(s -> !s.isBlank()).ifPresent(topic::setName);
            Optional.ofNullable(request.getDescription()).filter(s -> !s.isBlank()).ifPresent(topic::setDescription);
            Optional.ofNullable(request.getCourseId()).ifPresent(topic::setModuleId);

            topic.setUpdated(new Date());
            // Оновлюємо сутність в базі
            topicDao.update(topic);
            return topicToUpdateTopicResponseConverter.convert(topic);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ProgPlatformException(ErrorConsts.DATA_NOT_FOUND);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteTopicResponse delete(int id) {
        topicDao.delete(id);
        return new DeleteTopicResponse(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAllCourseModules getAllModuleTopics(int moduleId) {
        try {
            moduleDao.getById(moduleId);
            List<Topic> courseTopics = topicDao.findAllTopicsByModuleId(moduleId);
            return new GetAllCourseModules(courseTopics);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ProgPlatformException(ErrorConsts.MODULE_NOT_FOUND);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopicResponse getTopicById(int topicId) {
        try {
            // Отримуємо тему по ID
            Topic topic = topicDao.getById(topicId);
            return topicResponseTopicConverter.convert(topic);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ProgPlatformException(ErrorConsts.TOPIC_NOT_FOUND);
        }
    }
}
