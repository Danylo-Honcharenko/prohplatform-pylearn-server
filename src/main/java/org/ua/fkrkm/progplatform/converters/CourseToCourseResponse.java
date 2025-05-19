package org.ua.fkrkm.progplatform.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.dao.*;
import org.ua.fkrkm.proglatformdao.entity.*;
import org.ua.fkrkm.proglatformdao.entity.Module;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.proglatformdao.entity.view.TopicView;
import org.ua.fkrkm.proglatformdao.entityMongo.Question;
import org.ua.fkrkm.proglatformdao.entityMongo.Test;
import org.ua.fkrkm.proglatformdao.entityMongo.view.QuestionView;
import org.ua.fkrkm.proglatformdao.entityMongo.view.TestView;
import org.ua.fkrkm.progplatformclientlib.response.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseToCourseResponse implements Converter<Course, CourseResponse> {

    // DAO для роботи з темами
    private final TopicDaoI topicDao;
    // DAO для роботи з тестами
    private final TestDaoI testDao;
    // DAO для роботи з модулями
    private final ModuleDaoI moduleDao;
    // DAO для роботи зі статистикой по модулю
    private final ModuleStatDaoI moduleStatDao;
    // DAO для роботи із завданнями
    private final ExerciseDaoI exerciseDao;

    @Override
    public CourseResponse convert(Course source) {
        return CourseResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .created(source.getCreated())
                .updated(source.getUpdated())
                .modules(this.getModulesByCourseId(source.getId(), new ArrayList<>()))
                .build();
    }

    /**
     * Конвертор
     *
     * @param source вхідний об'єкт
     * @param userId ID користувача
     * @return CourseResponse вихідний об'єкт
     */
    public CourseResponse convert(Course source, Integer userId) {
        List<ModuleStat> moduleStats = moduleStatDao.findModuleStatByUserId(userId);
        return CourseResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .created(source.getCreated())
                .updated(source.getUpdated())
                .modules(this.getModulesByCourseId(source.getId(), moduleStats))
                .build();
    }

    /**
     * Отримати модулі по ID курсу
     *
     * @param courseId ID курсу
     * @param moduleStats статистика модуля по ID користувача
     * @return List<ModuleView> перегляд модулів
     */
    private List<ModuleView> getModulesByCourseId(int courseId, List<ModuleStat> moduleStats) {
        List<Module> modules = moduleDao.getModulesByCourseId(courseId);
        return modules.stream()
                .sorted(Comparator.comparingInt(Module::getId))
                .map(m -> this.moduleToModuleView(m, moduleStats))
                .toList();
    }

    /**
     * Конвертор Module у ModuleView
     *
     * @param module модуль
     * @param moduleStats статистика модуля по ID користувача
     * @return ModuleView перегляд модуля
     */
    private ModuleView moduleToModuleView(Module module, List<ModuleStat> moduleStats) {
        List<TopicView> moduleTopics = this.getModuleTopics(module.getId(), moduleStats);
        BigDecimal completion = this.calculatePercentageModuleCompletion(moduleTopics);
        return ModuleView.builder()
                .id(module.getId())
                .name(module.getName())
                .description(module.getDescription())
                .topics(moduleTopics)
                .complete(completion)
                .created(module.getCreated())
                .updated(module.getUpdated())
                .build();
    }

    /**
     * Вираховуємо процент проходження модуля (максимально 100%)
     *
     * @param moduleTopics теми модуля
     * @return BigDecimal процент проходження модуля
     */
    private BigDecimal calculatePercentageModuleCompletion(List<TopicView> moduleTopics) {
        // Отримуємо всі пройдені теми
        float doneTopics = (float) moduleTopics.stream()
                .filter(TopicView::getDone)
                .count();
        // Отримуємо всі теми модуля
        float allTopics = (float) moduleTopics.size();
        // Перевіряємо що пройдені теми та список всіх тем присутні
        if (doneTopics != 0F && allTopics != 0F) {
            BigDecimal percentageModuleCompletion = BigDecimal.valueOf((doneTopics / allTopics) * 100);

            return percentageModuleCompletion.round(new MathContext(4, RoundingMode.HALF_UP));
        }
        return BigDecimal.ZERO;
    }

    /**
     * Отримати всі теми модуля
     *
     * @param moduleId ID модуля
     * @param moduleStats статистика модуля по ID користувача
     * @return List<TopicView> теми модуля
     */
    private List<TopicView> getModuleTopics(int moduleId, List<ModuleStat> moduleStats) {
        return topicDao.findAllTopicsByModuleId(moduleId).stream()
                .sorted(Comparator.comparingInt(Topic::getId))
                .map(t -> this.topicToTopicView(t, moduleStats))
                .toList();
    }

    /**
     * Конвертор Topic у TopicView
     *
     * @param topic тема
     * @param moduleStats статистика модуля по ID користувача
     * @return TopicView відображення теми
     */
    private TopicView topicToTopicView(Topic topic, List<ModuleStat> moduleStats) {
        List<TestView> tests = testDao.getByTopicId(topic.getId()).stream()
                .map(this::testToTestView)
                .toList();

        List<Exercise> exercises = exerciseDao.findExercisesByTopicId(topic.getId());

        return TopicView.builder()
                .id(topic.getId())
                .name(topic.getName())
                .description(topic.getDescription())
                .moduleId(topic.getModuleId())
                .tests(tests)
                // Отримуємо лиш одне завдання
                .exercise(exercises.isEmpty() ? null : exercises.getFirst())
                .done(this.checkIsTopicDone(moduleStats, topic.getId()))
                .created(topic.getCreated())
                .updated(topic.getUpdated())
                .build();

    }

    /**
     * Перевіряємо статус перегляду теми true - переглянута/пройдена, false - не переглянуто
     *
     * @param moduleStats статистика по модулю
     * @param topicId ID теми
     * @return Boolean true/false
     */
    private Boolean checkIsTopicDone(List<ModuleStat> moduleStats, int topicId) {
        if (moduleStats.isEmpty()) return false;
        for (ModuleStat moduleStat : moduleStats) {
            if (moduleStat.getTopicId() == topicId) return true;
        }
        return false;
    }

    /**
     * Конвертор Test у TestView
     *
     * @param test тест
     * @return TestView відображення тесту
     */
    private TestView testToTestView(Test test) {
        return TestView.builder()
                .uuid(test.getId())
                .name(test.getName())
                .questions(questionsToQuestionsView(test.getQuestions()))
                .created(test.getCreated())
                .updated(test.getUpdated())
                .build();
    }

    /**
     * Конвертація List<Question> у List<QuestionView>
     *
     * @param questions питання
     * @return List<QuestionView> сконвертовані питання
     */
    private List<QuestionView> questionsToQuestionsView(List<Question> questions) {
        return questions.stream()
                .map(this::questionToQuestionView)
                .toList();
    }

    /**
     * Конвертація Question у QuestionView
     *
     * @param question питання
     * @return QuestionView сконвертоване питання
     */
    private QuestionView questionToQuestionView(Question question) {
        return QuestionView.builder()
                .question(question.getQuestion())
                .options(question.getOptions())
                .build();
    }
}
