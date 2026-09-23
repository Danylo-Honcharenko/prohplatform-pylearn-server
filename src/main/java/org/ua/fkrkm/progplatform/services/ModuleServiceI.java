package org.ua.fkrkm.progplatform.services;

import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

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
     * Встановити пройдену тему модуля
     *
     * @param request запит
     * @return CreateModuleCompleteResponse відповідь API
     */
    SetModuleTopicCompletedResponse setCompletedModuleTopic(SetModuleTopicCompletedRequest request);
}
