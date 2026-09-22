package org.ua.fkrkm.progplatform.converters;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.proglatformdao.entity.view.TopicView;

@Component
public class TopicToTopicView implements Converter<Topic, TopicView>{

    @Override
    public TopicView convert(@NonNull Topic topic) {
        return TopicView.builder()
                .id(topic.getId())
                .name(topic.getName())
                .description(topic.getDescription())
                .build();
    }
}
