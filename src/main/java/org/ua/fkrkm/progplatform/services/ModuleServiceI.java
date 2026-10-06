package org.ua.fkrkm.progplatform.services;

import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;

/**
 * Інтерфейс для роботи з модулями
 */
public interface ModuleServiceI {
    /**
     * Отримати модулі по ID курсу
     *
     * @param courseId ID курсу
     * @return ModulesResponse відповідь API
     */
    ModulesResponse getModulesByCourseId(Long courseId);

    /**
     * Отримати модуль по ID
     *
     * @param moduleId ID модуля
     * @return ModuleResponse відповідь API
     */
    ModuleResponse getModuleById(Long moduleId);

    /**
     * Встановити пройдену тему модуля
     *
     * @param request запит
     * @return CreateModuleCompleteResponse відповідь API
     */
    SetModuleTopicCompletedResponse setCompletedModuleTopic(SetModuleTopicCompletedRequest request);
}
