package org.ua.fkrkm.progplatform.converters;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.Course;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.List;

@Component
@AllArgsConstructor
public class CourseToCourseResponse implements MultiConverter<Course, CourseResponse> {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CourseResponse> convert(List<Course> source) {
        return source.stream()
                .map(this::convert)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseResponse convert(@NonNull Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .created(course.getCreated())
                .updated(course.getUpdated())
                .build();
    }

//    /**
//     * Отримати теми модулів
//     *
//     * @param modules модулі
//     * @return List<Topic> теми
//     */
//    private List<Topic> getTopicsByModules(List<Module> modules) {
//        if (modules.isEmpty()) return new ArrayList<>();
//
//        List<Long> moduleIds = modules.stream()
//                .map(Module::getId)
//                .toList();
//
//        return this.topicDao.findAllTopicsByModuleIdList(moduleIds);
//    }

//    /**
//     * Зібрати відповідь
//     *
//     * @param courses курси
//     * @return List<CourseResponse> зібрана відповідь
//     */
//    private List<CourseResponse> convertList(List<Course> courses) {
//
//        Map<Long, Course> courseIdToCourse = courses.stream()
//                .collect(Collectors.toMap(Course::getId, (course) -> course));
//
//        Map<Long, List<Module>> courseIdToModule = courses.stream()
//                .flatMap((course) -> this.moduleDao.getModulesByCourseId(course.getId()).stream())
//                .collect(Collectors.groupingBy(Module::getCourseId));
//
//        List<Module> modules = courseIdToModule.values().stream()
//                .flatMap(Collection::stream)
//                .toList();
//
//        Map<Long, List<Topic>> moduleIdToTopic = this.getTopicsByModules(modules).stream()
//                .collect(Collectors.groupingBy(Topic::getModuleId));
//
//        return courseIdToModule.keySet().stream()
//                .map((courseId) -> new Group(courseIdToCourse.getOrDefault(courseId, Course.builder().build()), courseIdToModule.getOrDefault(courseId, new ArrayList<>()), null))
//                .peek((group) -> {
//                    List<Topic> topics = group.getModules().stream()
//                            .map((module) -> moduleIdToTopic.get(module.getId()))
//                            .flatMap(Collection::stream)
//                            .toList();
//                    group.setTopics(topics);
//                })
//                .map((group) -> convert(group.getCourse(), group.getModules(), group.getTopics()))
//                .toList();
//    }

//    /**
//     * Клас для групування обʼєктів
//     */
//    @Data
//    @AllArgsConstructor
//    private static class Group {
//        private Course course;
//        private List<Module> modules;
//        private List<Topic> topics;
//    }

}
