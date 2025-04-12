package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.progplatformclientlib.response.*;

@Component
public class CourseToCreateCourseResponse implements Converter<Course, CreateCourseResponse> {

    @Override
    public CreateCourseResponse convert(Course source) {
        return CreateCourseResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .created(source.getCreated())
                .build();
    }
}
