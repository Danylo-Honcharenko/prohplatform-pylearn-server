package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Exercise;
import org.ua.fkrkm.progplatformclientlib.response.*;

@Component
public class ExerciseToCreateExerciseResponse implements Converter<Exercise, CreateExerciseResponse> {

    @Override
    public CreateExerciseResponse convert(Exercise source) {
        return CreateExerciseResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .topicId(source.getTopicId())
                .created(source.getCreated())
                .build();
    }
}
