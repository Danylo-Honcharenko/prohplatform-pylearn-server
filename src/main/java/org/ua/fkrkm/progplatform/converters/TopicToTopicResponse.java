package org.ua.fkrkm.progplatform.converters;

import org.ua.fkrkm.progplatformclientlib.data.TopicData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.progplatformclientlib.response.*;

@Component
public class TopicToTopicResponse implements Converter<Topic, TopicResponse> {

    @Override
    public TopicResponse convert(Topic source) {
        return new TopicResponse(TopicData.builder()
                .name(source.getName())
                .description(source.getDescription())
                .courseId(source.getModuleId())
                .created(source.getCreated())
                .updated(source.getUpdated())
                .build());
    }
}
