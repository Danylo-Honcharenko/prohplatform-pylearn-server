package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Exercise;
import org.ua.fkrkm.progplatformclientlib.request.*;

import java.util.Date;

@Component
public class CreateExerciseRequestToExercise implements Converter<CreateExerciseRequest, Exercise> {

    @Override
    public Exercise convert(CreateExerciseRequest source) {
        return Exercise.builder()
                .name(source.getName())
                .description(source.getDescription())
                .topicId(source.getTopicId())
                .created(new Date())
                .build();
    }
}
