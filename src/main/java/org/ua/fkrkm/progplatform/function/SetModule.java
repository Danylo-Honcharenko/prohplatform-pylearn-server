package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.entity.Module;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.List;
import java.util.function.Consumer;

/**
 * Проставляє модулі
 */
public class SetModule implements Consumer<CourseResponse> {
    // Модулі
    private final List<Module> modules;

    /**
     * Конструктор
     *
     * @param modules модулі
     */
    public SetModule(List<Module> modules) {
        this.modules = modules;
    }

    /**
     * {@inheritDoc}
     */
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
