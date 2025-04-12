package org.ua.fkrkm.progplatform.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.ModuleStatDaoI;
import org.ua.fkrkm.proglatformdao.entity.ModuleStat;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.ModuleServiceI;

/**
 * Сервіс для роботи з модулями
 */
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleServiceI {

    // DAO для роботи зі статистикой по модулях
    private final ModuleStatDaoI moduleStatDao;
    // Конвертор
    private final Converter<SetModuleTopicCompletedRequest, ModuleStat> createModuleCompleteRequestModuleStatConverter;
    // Конвертор
    private final Converter<ModuleStat, SetModuleTopicCompletedResponse> createModuleCompleteResponseModuleStatConverter;

    /**
     * {@inheritDoc}
     */
    @Override
    public SetModuleTopicCompletedResponse setCompletedModuleTopic(SetModuleTopicCompletedRequest request) {
        ModuleStat moduleStat = createModuleCompleteRequestModuleStatConverter.convert(request);
        int id = moduleStatDao.create(moduleStat);
        moduleStat.setId(id);
        return createModuleCompleteResponseModuleStatConverter.convert(moduleStat);
    }
}
