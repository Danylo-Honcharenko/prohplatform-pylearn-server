package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.entity.Module;
import org.ua.fkrkm.proglatformdao.entity.ModuleStat;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.proglatformdao.entity.view.TopicView;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SetModuleByCourseId implements Consumer<CourseResponse> {

    private final List<Module> modules;

    public SetModuleByCourseId(Supplier<List<Module>> supplier) {
        this.modules = supplier.get();
    }

    @Override
    public void accept(CourseResponse courseResponse) {
        if (!this.modules.isEmpty()) {
            List<ModuleView> moduleViews = modules.stream()
                    .map(this::moduleToModuleView)
                    .toList();
            courseResponse.setModules(moduleViews);
        }
    }

    /**
     * Конвертор Module у ModuleView
     *
     * @param module модуль
     * @return ModuleView перегляд модуля
     */
    private ModuleView moduleToModuleView(Module module) {
        return ModuleView.builder()
                .id(module.getId())
                .name(module.getName())
                .description(module.getDescription())
                .created(module.getCreated())
                .updated(module.getUpdated())
                .build();
    }
}
