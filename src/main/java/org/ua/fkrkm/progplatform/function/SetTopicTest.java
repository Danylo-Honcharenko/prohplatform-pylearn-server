package org.ua.fkrkm.progplatform.function;

import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.proglatformdao.entity.view.TopicView;
import org.ua.fkrkm.proglatformdao.entityMongo.Question;
import org.ua.fkrkm.proglatformdao.entityMongo.Test;
import org.ua.fkrkm.proglatformdao.entityMongo.view.QuestionView;
import org.ua.fkrkm.proglatformdao.entityMongo.view.TestView;
import org.ua.fkrkm.progplatformclientlib.response.CourseResponse;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SetTopicTest implements Consumer<CourseResponse> {

    private final Function<List<Integer>, List<Test>> function;

    public SetTopicTest(Function<List<Integer>, List<Test>> function) {
        this.function = function;
    }

    @Override
    public void accept(CourseResponse courseResponse) {
        List<ModuleView> modules = courseResponse.getModules();
        if (!modules.isEmpty()) {
            List<ModuleView> moduleViews = modules.stream()
                    .peek(this::getTopic)
                    .toList();
            courseResponse.setModules(moduleViews);
        }
    }

    private void getTopic(ModuleView module) {
        List<TopicView> topics = module.getTopics();
        List<Integer> viewsIds = topics.stream()
                .map(TopicView::getId)
                .toList();
        List<TestView> tests = function.apply(viewsIds).stream()
                .map(this::testToTestView)
                .toList();
        List<TopicView> topicViews = topics.stream()
                .peek((topic) -> this.setTest(topic, tests))
                .toList();
        module.setTopics(topicViews);
    }

    private void setTest(TopicView topicView, List<TestView> tests) {
        Optional<TestView> testView = tests.stream()
                .filter((test) -> test.getTopicId().equals(topicView.getId()))
                .findFirst();

        if (testView.isPresent()) {
            topicView.setTests(List.of(testView.get()));
        }
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
                .topicId(test.getTopicId())
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
