package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.ModuleStat;
import org.ua.fkrkm.progplatformclientlib.request.*;

@Component
public class CreateModuleCompleteRequestToModuleStat implements Converter<SetModuleTopicCompletedRequest, ModuleStat> {

    @Override
    public ModuleStat convert(SetModuleTopicCompletedRequest source) {
        return ModuleStat.builder()
                .moduleId(source.getModuleId())
                .topicId(source.getTopicId())
                .userId(source.getUserId())
                .build();
    }
}
