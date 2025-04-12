package org.ua.fkrkm.progplatform.services;

import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

/**
 * Інтерфейс для роботи з модулями
 */
public interface ModuleServiceI {
    /**
     * Встановити пройдену тему модуля
     *
     * @param request запит
     * @return CreateModuleCompleteResponse відповідь API
     */
    SetModuleTopicCompletedResponse setCompletedModuleTopic(SetModuleTopicCompletedRequest request);
}
