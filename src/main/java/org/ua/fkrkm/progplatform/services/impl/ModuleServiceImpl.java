package org.ua.fkrkm.progplatform.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.ModuleDaoI;
import org.ua.fkrkm.proglatformdao.dao.ModuleStatDaoI;
import org.ua.fkrkm.proglatformdao.entity.Module;
import org.ua.fkrkm.proglatformdao.entity.ModuleStat;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleStateView;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.services.ModuleServiceI;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Сервіс для роботи з модулями
 */
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleServiceI {
    // DAO для роботи з модулями
    private final ModuleDaoI moduleDao;
    // DAO для роботи зі статистиком по модулях
    private final ModuleStatDaoI moduleStatDao;
    // Конвертор
    private final Converter<SetModuleTopicCompletedRequest, ModuleStat> createModuleCompleteRequestModuleStatConverter;
    // Конвертор
    private final Converter<ModuleStat, SetModuleTopicCompletedResponse> createModuleCompleteResponseModuleStatConverter;
    // Конвертор
    private final Converter<Module, ModuleView> moduleToModuleViewConverter;
    // Сервіс для роботи з поточним користувачем в системі
    private final AuthUserServiceI authUserService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ModulesResponse getModulesByCourseId(Long courseId) {
        User authUser = this.authUserService.getCurrentAuthUser();
        List<ModuleStateView> moduleStateViews = moduleStatDao.findModulesStatByUserId(authUser.getId());

        List<ModuleView> modules = this.moduleDao.getModulesByCourseId(courseId).stream()
                .map(this.moduleToModuleViewConverter::convert)
                .peek((module) -> this.setModuleCompletePercent(module, moduleStateViews))
                .toList();
        return new ModulesResponse(modules);
    }

    /**
     * Проставити процент проходження модуля
     *
     * @param module модуль
     */
    private void setModuleCompletePercent(ModuleView module, List<ModuleStateView> moduleStateViews) {
        Optional<ModuleStateView> moduleStat = moduleStateViews.stream()
                .filter((moduleStateView) -> moduleStateView.getModuleId() == module.getId())
                .findFirst();
        if (moduleStat.isPresent()) {
            ModuleStateView moduleStateView = moduleStat.get();
            module.setComplete(moduleStateView.getModuleCompletionPercentage());
        } else {
            module.setComplete(BigDecimal.ZERO);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SetModuleTopicCompletedResponse setCompletedModuleTopic(SetModuleTopicCompletedRequest request) {
        ModuleStat moduleStat = this.createModuleCompleteRequestModuleStatConverter.convert(request);
        Long id = (long) this.moduleStatDao.create(moduleStat);
        moduleStat.setId(id);
        return this.createModuleCompleteResponseModuleStatConverter.convert(moduleStat);
    }
}
