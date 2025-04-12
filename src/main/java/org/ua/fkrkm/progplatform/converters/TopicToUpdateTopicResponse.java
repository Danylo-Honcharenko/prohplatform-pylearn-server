package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.progplatformclientlib.response.*;

@Component
public class TopicToUpdateTopicResponse implements Converter<Topic, UpdateTopicResponse> {

    @Override
    public UpdateTopicResponse convert(Topic source) {
        return UpdateTopicResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .updated(source.getUpdated())
                .build();
    }
}
