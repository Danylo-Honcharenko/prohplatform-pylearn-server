package org.ua.fkrkm.progplatform.function;

import org.springframework.util.Assert;
import org.ua.fkrkm.proglatformdao.entityMongo.Question;
import org.ua.fkrkm.proglatformdao.entityMongo.view.AnswerView;

import java.util.List;
import java.util.function.Predicate;

/**
 * Клас перевірки відповідей
 */
public class CheckAnswers implements Predicate<AnswerView> {
    // Список питань
    private final List<Question> questions;

    /**
     * Конструктор
     *
     * @param questions питання
     */
    public CheckAnswers(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean test(AnswerView answerView) {
        Assert.notNull(answerView,
                "AnswerView must not be null!");
        for (Question question : questions) {
            if (question.getQuestion().equals(answerView.getQuestion())) {
                return question.getCorrectAnswer().equals(answerView.getAnswer());
            }
        }
        return false;
    }
}
