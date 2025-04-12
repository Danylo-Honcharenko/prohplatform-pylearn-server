package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entityMongo.Question;
import org.ua.fkrkm.proglatformdao.entityMongo.Test;
import org.ua.fkrkm.proglatformdao.entityMongo.view.QuestionView;
import org.ua.fkrkm.proglatformdao.entityMongo.view.TestView;

import java.util.List;

@Component
public class TestViewToTest implements Converter<Test, TestView> {

    @Override
    public TestView convert(Test source) {
        return TestView.builder()
                .uuid(source.getId())
                .name(source.getName())
                .topicId(source.getTopicId())
                .questions(questionsToQuestionsView(source.getQuestions()))
                .created(source.getCreated())
                .updated(source.getUpdated())
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
