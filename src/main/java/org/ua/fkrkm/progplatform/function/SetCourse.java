package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SetCourse implements Consumer<CourseResponse> {

    private final Course course;

    public SetCourse(Supplier<Course> course) {
        this.course = course.get();
    }

    @Override
    public void accept(CourseResponse courseResponse) {
        if (course != null) {
            courseResponse.setId(course.getId());
            courseResponse.setName(course.getName());
            courseResponse.setDescription(course.getDescription());
            courseResponse.setCreated(course.getCreated());
            courseResponse.setUpdated(course.getUpdated());
        }
    }
}
