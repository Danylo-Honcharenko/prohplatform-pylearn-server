package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.progplatformclientlib.request.*;

import java.util.Date;

@Component
public class CreateTopicRequestToTopic implements Converter<CreateTopicRequest, Topic> {

    @Override
    public Topic convert(CreateTopicRequest source) {
        return Topic.builder()
                .name(source.getName())
                .description(source.getDescription())
                .moduleId(source.getModuleId())
                .created(new Date())
                .build();
    }
}
