package org.ua.fkrkm.progplatform.converters;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Module;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;

@Component
public class ModuleToModuleView implements Converter<Module, ModuleView> {

    @Override
    public ModuleView convert(@NonNull Module source) {
        return ModuleView.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .build();
    }
}
