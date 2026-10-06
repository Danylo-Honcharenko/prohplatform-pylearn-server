package org.ua.fkrkm.progplatform.converters;

import org.ua.fkrkm.progplatformclientlib.data.ModuleData;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.progplatformclientlib.response.ModuleResponse;

@Component
public class ModuleViewToModuleResponse implements Converter<ModuleView, ModuleResponse> {

    @Override
    public ModuleResponse convert(@NonNull ModuleView source) {
        return new ModuleResponse(ModuleData.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .complete(source.getComplete())
                .active(source.getActive())
                .created(source.getCreated())
                .updated(source.getUpdated())
                .build());
    }
}
