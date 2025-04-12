package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.progplatformclientlib.request.*;

import java.util.Date;

/**
 * Клас конвертор "CreateCourseRequest" в "Course"
 */
@Component
public class CreateCourseRequestToCourse implements Converter<CreateCourseRequest, Course> {

    @Override
    public Course convert(CreateCourseRequest source) {
        return Course.builder()
                .name(source.getName())
                .description(source.getDescription())
                .created(new Date())
                .build();
    }
}
