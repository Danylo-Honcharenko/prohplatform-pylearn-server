package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entityMongo.Question;
import org.ua.fkrkm.proglatformdao.entityMongo.Test;
import org.ua.fkrkm.proglatformdao.entityMongo.view.QuestionView;
import org.ua.fkrkm.progplatformclientlib.response.*;

import java.util.List;

/**
 * Конвертер
 */
@Component
public class TestToGetTestResponse implements Converter<Test, GetTestResponse> {

    @Override
    public GetTestResponse convert(Test source) {
        return GetTestResponse.builder()
                .name(source.getName())
                .questions(questionsToQuestionsView(source.getQuestions()))
                .created(source.getCreated())
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
