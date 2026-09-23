package org.ua.fkrkm.progplatform.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.ModuleDaoI;
import org.ua.fkrkm.proglatformdao.dao.ModuleStatDaoI;
import org.ua.fkrkm.proglatformdao.dao.TopicDaoI;
import org.ua.fkrkm.proglatformdao.entity.Module;
import org.ua.fkrkm.proglatformdao.entity.ModuleStat;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.proglatformdao.entity.view.TopicView;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformNotFoundException;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;
import org.ua.fkrkm.progplatform.services.CourseServiceI;
import org.ua.fkrkm.progplatform.services.TopicServiceI;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Сервіс для роботи з темами
 */
@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicServiceI {

    // DAO для роботи з модулями
    private final ModuleDaoI moduleDao;
    // DAO для роботи зі статистикой по модулю
    private final ModuleStatDaoI moduleStatDao;
    // Сервіс для роботи з курсами
    private final CourseServiceI courseService;
    // DAO для роботи з темами
    private final TopicDaoI topicDao;
    // Сервіс для роботи з поточним користувачем у системі
    private final AuthUserServiceI authUserService;
    // Конвертор
    private final Converter<CreateTopicRequest, Topic> createTopicRequestTopicConverter;
    // Конвертор
    private final Converter<Topic, CreateTopicResponse> createTopicResponseTopicConverter;
    // Конвертор
    private final Converter<Topic, UpdateTopicResponse> topicToUpdateTopicResponseConverter;
    // Конвертор
    private final Converter<Topic, TopicResponse> topicResponseTopicConverter;
    // Конвертор
    private final Converter<Topic, TopicView> topicTopicViewConverter;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateTopicResponse create(CreateTopicRequest request) {
        Topic topic = createTopicRequestTopicConverter.convert(request);
        // Створюємо тему
        Long id = (long) topicDao.create(topic);
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
        Long userId = currentAuthUser.getId();
        Long courseId = request.getCourseId();
        if (courseId != null) {
            boolean userExistsInCourse = courseService.checkIfUserExistsInCourse(courseId, userId);
            // Перевіряємо, що поточний користувач є в цьому списку
            if (userExistsInCourse && !authUserService.isCurrentAuthUserAdmin())
                throw new ProgPlatformException(ErrorConsts.INSUFFICIENT_RIGHTS);
        }
        // Отримуємо тему по ID
        List<Topic> topics = topicDao.getById(request.getId());
        if (topics.isEmpty()) throw new ProgPlatformNotFoundException(ErrorConsts.TOPIC_NOT_FOUND);
        Topic topic = topics.getFirst();

        // Заповнюємо оновлені дані, якщо вони є
        Optional.ofNullable(request.getName()).filter(s -> !s.isBlank()).ifPresent(topic::setName);
        Optional.ofNullable(request.getDescription()).filter(s -> !s.isBlank()).ifPresent(topic::setDescription);
        Optional.ofNullable(request.getCourseId()).ifPresent(topic::setModuleId);

        topic.setUpdated(new Date());
        // Оновлюємо сутність у базі
        topicDao.update(topic);
        return topicToUpdateTopicResponseConverter.convert(topic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeleteTopicResponse delete(Long id) {
        topicDao.delete(id);
        return new DeleteTopicResponse(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAllModuleTopics getAllModuleTopics(Long moduleId) {
        List<Module> modules = this.moduleDao.getById(moduleId);
        if (modules.isEmpty()) throw new ProgPlatformNotFoundException(ErrorConsts.MODULE_NOT_FOUND);

        User authUser = authUserService.getCurrentAuthUser();
        List<ModuleStat> moduleStats = this.moduleStatDao.findModuleStatByUserId(authUser.getId());

        AtomicInteger sequence = new AtomicInteger(0);
        List<TopicView> topics = this.topicDao.findAllTopicsByModuleId(moduleId).stream()
                .map(this.topicTopicViewConverter::convert)
                .sorted(Comparator.comparingLong(TopicView::getId))
                .peek((topicView) -> topicView.setPage(sequence.incrementAndGet()))
                .peek((topicView) -> topicView.setDone(this.checkIsTopicDone(topicView.getId(), moduleStats)))
                .toList();

        return new GetAllModuleTopics(topics);
    }

    /**
     * Перевіряємо статус перегляду теми true - переглянута/пройдена, false - не переглянуто
     *
     * @param topicId ID теми
     * @return Boolean true/false
     */
    private Boolean checkIsTopicDone(Long topicId, List<ModuleStat> moduleStats) {
        if (moduleStats.isEmpty()) return false;
        for (ModuleStat moduleStat : moduleStats) {
            if (moduleStat.getTopicId().equals(topicId)) return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopicResponse getTopicById(Long topicId) {
        // Отримуємо тему по ID
        List<Topic> topics = topicDao.getById(topicId);
        if (topics.isEmpty()) throw new ProgPlatformNotFoundException(ErrorConsts.TOPIC_NOT_FOUND);
        Topic topic = topics.getFirst();
        return topicResponseTopicConverter.convert(topic);
    }
}
