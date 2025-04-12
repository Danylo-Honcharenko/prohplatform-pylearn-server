package org.ua.fkrkm.progplatform.converters;

import org.ua.fkrkm.proglatformdao.entityMongo.Question;
import org.ua.fkrkm.proglatformdao.entityMongo.view.AnswerView;

import java.util.List;
import java.util.function.Function;

public class AnswerViewToQuestion implements Function<AnswerView, Question> {

    private final List<Question> questions;

    public AnswerViewToQuestion(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public Question apply(AnswerView answerView) {
        for (Question question : questions) {
            if (answerView.getQuestion().equals(question.getQuestion())) {
                return question;
            }
        }
        return Question.builder().build();
    }
}
