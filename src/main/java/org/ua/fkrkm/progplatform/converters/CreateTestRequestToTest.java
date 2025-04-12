package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entityMongo.Test;
import org.ua.fkrkm.progplatformclientlib.request.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CreateTestRequestToTest implements Converter<CreateTestRequest, Test> {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    public Test convert(CreateTestRequest source) {
        return Test.builder()
                .name(source.getName())
                .topicId(source.getTopicId())
                .questions(source.getQuestions())
                .created(simpleDateFormat.format(new Date()))
                .build();
    }
}
