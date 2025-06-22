package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.entity.view.ModuleStateView;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SetModulePercent implements Consumer<CourseResponse> {

    private final List<ModuleStateView> moduleStateViews;

    public SetModulePercent(Supplier<List<ModuleStateView>> supplier) {
        this.moduleStateViews = supplier.get();
    }

    @Override
    public void accept(CourseResponse courseResponse) {
        List<ModuleView> modules = courseResponse.getModules();
        if (!modules.isEmpty()) {
            List<ModuleView> moduleViews = modules.stream()
                    .map(this::setModuleCompletePercent)
                    .toList();
            courseResponse.setModules(moduleViews);
        }
    }

    /**
     * Проставити процент проходження модуля
     *
     * @param module модуль
     * @return ModuleView інформація по модулю
     */
    private ModuleView setModuleCompletePercent(ModuleView module) {
        Optional<ModuleStateView> moduleStat = this.moduleStateViews.stream()
                .filter((moduleStateView) -> moduleStateView.getModuleId() == module.getId())
                .findFirst();
        if (moduleStat.isEmpty()) {
            module.setComplete(BigDecimal.ZERO);
            return module;
        }
        ModuleStateView moduleStateView = moduleStat.get();
        module.setComplete(moduleStateView.getModuleCompletionPercentage());
        return module;
    }
}
