package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SetCourse implements Consumer<CourseResponse> {

    private final List<Course> courses;

    public SetCourse(Supplier<List<Course>> course) {
        this.courses = course.get();
    }

    @Override
    public void accept(CourseResponse courseResponse) {
        if (this.courses.isEmpty()) throw new ProgPlatformException(ErrorConsts.COURSE_NOT_FOUND);
        Course course = courses.getFirst();
        courseResponse.setId(course.getId());
        courseResponse.setName(course.getName());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setCreated(course.getCreated());
        courseResponse.setUpdated(course.getUpdated());
    }
}
