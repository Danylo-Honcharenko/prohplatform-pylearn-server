package org.ua.fkrkm.progplatform.services;

import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

/**
 * Інтерфейс для роботи з темами
 */
public interface TopicServiceI {
    /**
     * Створити тему
     *
     * @param request запит
     * @return CreateTopicResponse відповідь API
     */
    CreateTopicResponse create(CreateTopicRequest request);
    /**
     * Оновити тему
     *
     * @param request запит
     * @return UpdateTopicResponse відповідь API
     */
    UpdateTopicResponse update(UpdateTopicRequest request);
    /**
     * Видалити теми
     *
     * @param id ID теми
     * @return DeleteTopicResponse відповідь API
     */
    DeleteTopicResponse delete(Long id);
    /**
     * Отримати всі теми модуля
     *
     * @param moduleId ID курсу
     * @return GetAllCourseTopics відповідь API
     */
    GetAllCourseModules getAllModuleTopics(Long moduleId);
    /**
     * Отримати тему по ID
     *
     * @param topicId ID теми
     * @return TopicResponse відповідь API
     */
    TopicResponse getTopicById(Long topicId);
}
