package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.ModuleStat;
import org.ua.fkrkm.progplatformclientlib.response.*;

@Component
public class ModuleStatToCreateModuleCompleteResponse implements Converter<ModuleStat, SetModuleTopicCompletedResponse> {

    @Override
    public SetModuleTopicCompletedResponse convert(ModuleStat source) {
        return SetModuleTopicCompletedResponse.builder()
                .id(source.getId())
                .userId(source.getUserId())
                .moduleId(source.getModuleId())
                .topicId(source.getTopicId())
                .build();
    }
}
