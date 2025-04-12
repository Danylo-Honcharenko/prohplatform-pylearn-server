package org.ua.fkrkm.progplatform.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entityMongo.Question;
import org.ua.fkrkm.proglatformdao.entityMongo.Test;
import org.ua.fkrkm.proglatformdao.entityMongo.view.QuestionView;
import org.ua.fkrkm.progplatformclientlib.response.*;

import java.util.List;

@Component
public class TestCreateTestResponse implements Converter<Test, CreateTestResponse> {

    @Override
    public CreateTestResponse convert(Test source) {
        return CreateTestResponse.builder()
                .uuid(source.getId())
                .name(source.getName())
                .topicId(source.getTopicId())
                .questions(questionsToQuestionsView(source.getQuestions()))
                .created(source.getCreated())
                .build();
    }

    private List<QuestionView> questionsToQuestionsView(List<Question> questions) {
        return questions.stream()
                .map(this::convertQuestionToQuestionView)
                .toList();
    }

    private QuestionView convertQuestionToQuestionView(Question question) {
        return QuestionView.builder()
                .question(question.getQuestion())
                .options(question.getOptions())
                .build();
    }
}
