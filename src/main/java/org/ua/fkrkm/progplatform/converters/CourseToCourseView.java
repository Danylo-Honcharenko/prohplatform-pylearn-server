package org.ua.fkrkm.progplatform.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.dao.TopicDaoI;
import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.proglatformdao.entity.view.CourseView;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseToCourseView implements Converter<Course, CourseView> {

    // DAO для роботи з темами
    private final TopicDaoI topicDao;

    @Override
    public CourseView convert(Course source) {
        return CourseView.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .topics(getCourseTopics(source.getId()))
                .created(source.getCreated())
                .build();
    }

    /**
     * Отримати всі теми курсу
     *
     * @param courseId ID курсу
     * @return List<Topic> теми курсу
     */
    public List<Topic> getCourseTopics(int courseId) {
        return topicDao.findAllTopicsByModuleId(courseId);
    }
}
