package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.progplatformclientlib.response.*;

@Component
public class TopicToCreateTopicResponse implements Converter<Topic, CreateTopicResponse> {

    @Override
    public CreateTopicResponse convert(Topic source) {
        return CreateTopicResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .created(source.getCreated())
                .build();
    }
}
