package org.ua.fkrkm.progplatform.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.entity.TestResult;
import org.ua.fkrkm.proglatformdao.entityMongo.Question;
import org.ua.fkrkm.proglatformdao.entityMongo.view.AnswerView;
import org.ua.fkrkm.progplatformclientlib.response.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestResultListToTestResultsResponseResponse implements Converter<List<TestResult>, TestResultsResponse> {
    // Google Gson
    private final Gson gson = new Gson();

    @Override
    public TestResultsResponse convert(List<TestResult> source) {
        return TestResultsResponse.builder()
                .results(convertListTestResultToListCheckTestAnswersResultResponse(source))
                .build();
    }

    /**
     * Конвертор
     *
     * @param source вхідний список
     * @return List<CheckTestAnswersResultResponse> вихідний список
     */
    private List<CheckTestAnswersResultResponse> convertListTestResultToListCheckTestAnswersResultResponse(List<TestResult> source) {
        return source.stream()
                .map(this::convertTestResultToCheckTestAnswersResultResponse)
                .toList();
    }

    /**
     * Конвертор
     *
     * @param source вхідний об'єкт
     * @return CheckTestAnswersResultResponse вихідний об'єкт
     */
    private CheckTestAnswersResultResponse convertTestResultToCheckTestAnswersResultResponse(TestResult source) {
        return CheckTestAnswersResultResponse.builder()
                .currentAssessment(source.getAssessment())
                .maxAssessment(source.getMaxAssessment())
                .correctAnswers(this.convertStringToListAnswerView(source.getCorrect()))
                .wrongAnswers(this.convertStringToListQuestion(source.getIncorrect()))
                .created(source.getCreated())
                .build();
    }

    /**
     * Конвертуємо строку у список об'єктів
     *
     * @param answer вхідна строка
     * @return List<AnswerView> список об'єктів
     */
    private List<AnswerView> convertStringToListAnswerView(String answer) {
        Type listOfMyClassObject = new TypeToken<ArrayList<AnswerView>>() {}.getType();
        return gson.fromJson(answer, listOfMyClassObject);
    }

    /**
     * Конвертуємо строку у список об'єктів
     *
     * @param question вхідна строка
     * @return List<Question> список об'єктів
     */
    private List<Question> convertStringToListQuestion(String question) {
        Type listOfMyClassObject = new TypeToken<ArrayList<Question>>() {}.getType();
        return gson.fromJson(question, listOfMyClassObject);
    }
}
