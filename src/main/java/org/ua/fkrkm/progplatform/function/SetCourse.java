package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Проставляє дані про курс
 */
public class SetCourse implements Consumer<CourseResponse> {
    // Курс
    private final Course course;

    /**
     * Конструктор
     *
     * @param course курс
     */
    public SetCourse(Course course) {
        this.course = course;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(CourseResponse courseResponse) {
        courseResponse.setId(this.course.getId());
        courseResponse.setName(this.course.getName());
        courseResponse.setDescription(this.course.getDescription());
        courseResponse.setCreated(this.course.getCreated());
        courseResponse.setUpdated(this.course.getUpdated());
    }
}
