package org.ua.fkrkm.progplatform.converters;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.dao.ModuleDaoI;
import org.ua.fkrkm.proglatformdao.dao.TopicDaoI;
import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.proglatformdao.entity.Module;
import org.ua.fkrkm.proglatformdao.entity.Topic;
import org.ua.fkrkm.progplatform.function.SetCourse;
import org.ua.fkrkm.progplatform.function.SetModule;
import org.ua.fkrkm.progplatform.function.SetTopic;
import org.ua.fkrkm.progplatform.utils.ObjectModifier;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CourseToCourseResponse implements MultiConverter<Course, CourseResponse> {

    // DAO для роботи з модулями
    private final ModuleDaoI moduleDao;
    // DAO для роботи з темами
    private final TopicDaoI topicDao;
    // DAO для роботи з тестами
//    private final TestDaoI testDao;

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public List<CourseResponse> convert(List<Course> source) {
        return this.convertList(source);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public CourseResponse convert(Course course) {
        // Отримуємо модулі по ID курсу
        List<Module> modules = this.moduleDao.getModulesByCourseId(course.getId());
        // Отримуємо теми по модулям
        List<Topic> topics = this.getTopicsByModules(modules);
        // Конвертуємо
        return this.convert(course, modules, topics);
    }

    /**
     * Отримати теми модулів
     *
     * @param modules модулі
     * @return List<Topic> теми
     */
    private List<Topic> getTopicsByModules(List<Module> modules) {
        if (modules.isEmpty()) return new ArrayList<>();

        List<Integer> moduleIds = modules.stream()
                .map(Module::getId)
                .toList();

        return this.topicDao.findAllTopicsByModuleIdList(moduleIds);
    }

    /**
     * Зібрати відповідь
     *
     * @param courses курси
     * @return List<CourseResponse> зібрана відповідь
     */
    private List<CourseResponse> convertList(List<Course> courses) {

        Map<Integer, Course> courseIdToCourse = courses.stream()
                .collect(Collectors.toMap(Course::getId, (course) -> course));

        Map<Integer, List<Module>> courseIdToModule = courses.stream()
                .flatMap((course) -> this.moduleDao.getModulesByCourseId(course.getId()).stream())
                .collect(Collectors.groupingBy(Module::getCourseId));

        List<Module> modules = courseIdToModule.values().stream()
                .flatMap(Collection::stream)
                .toList();

        Map<Integer, List<Topic>> moduleIdToTopic = this.getTopicsByModules(modules).stream()
                .collect(Collectors.groupingBy(Topic::getModuleId));

        return courseIdToModule.keySet().stream()
                .map((courseId) -> new Group(courseIdToCourse.getOrDefault(courseId, Course.builder().build()), courseIdToModule.getOrDefault(courseId, new ArrayList<>()), null))
                .peek((group) -> {
                    List<Topic> topics = group.getModules().stream()
                            .map((module) -> moduleIdToTopic.get(module.getId()))
                            .flatMap(Collection::stream)
                            .toList();
                    group.setTopics(topics);
                })
                .map((group) -> convert(group.getCourse(), group.getModules(), group.getTopics()))
                .toList();
    }

    /**
     * Клас для групування обʼєктів
     */
    @Data
    @AllArgsConstructor
    private static class Group {
        private Course course;
        private List<Module> modules;
        private List<Topic> topics;
    }

    /**
     * Конвертувати
     *
     * @param course курс
     * @param modules модулі до курсу
     * @param topics теми модулів
     * @return CourseResponse зібрана відповідь
     */
    private CourseResponse convert(Course course, List<Module> modules, List<Topic> topics) {
        // Заповнюємо об'єкт
        return ObjectModifier.init(new CourseResponse())
                // Проставляємо курс
                .apply(new SetCourse(course))
                // Проставляємо модулі
                .apply(new SetModule(modules))
                // Проставляємо теми
                .apply(new SetTopic(topics))
                // Встановлюємо тест
//                    .apply(new SetTest(this.testDao::getByTopicIds))
                // Отримуємо объект
                .get();
    }
}
