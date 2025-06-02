package org.ua.fkrkm.progplatform.services.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.TestDaoI;
import org.ua.fkrkm.proglatformdao.dao.TestResultDaoI;
import org.ua.fkrkm.proglatformdao.entity.TestResult;
import org.ua.fkrkm.proglatformdao.entityMongo.Question;
import org.ua.fkrkm.proglatformdao.entityMongo.Test;
import org.ua.fkrkm.proglatformdao.entityMongo.view.AnswerView;
import org.ua.fkrkm.proglatformdao.entityMongo.view.TestView;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformNotFoundException;
import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.converters.AnswerViewToQuestion;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.function.CheckAnswers;
import org.ua.fkrkm.progplatform.services.TestServiceI;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервіс для роботи з тестами
 */
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestServiceI {

    // DAO для роботи з тестами
    private final TestDaoI testDao;
    // Конвертор
    private final Converter<CreateTestRequest, Test> createTestRequestTestConverter;
    // Конвертор
    private final Converter<Test, CreateTestResponse> createTestResponseTestConverter;
    // Конвертор
    private final Converter<Test, GetTestResponse> testGetTestResponseConverter;
    // Конвертор
    private final Converter<Test, TestView> testTestViewConverter;
    // DAO для роботи з результатами тестування
    private final TestResultDaoI testResultDao;
    // Конвертор
    private final Converter<List<TestResult>, TestResultsResponse> testResultsResponseTestResultsConverter;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreateTestResponse create(CreateTestRequest request) {
        Test test = createTestRequestTestConverter.convert(request);
        String uuid = testDao.create(test);
        test.setId(uuid);
        return createTestResponseTestConverter.convert(test);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAllTestResponse getAll() {
        List<TestView> tests= testDao.getAll().stream()
                .map(testTestViewConverter::convert)
                .toList();
        return new GetAllTestResponse(tests);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetTestResponse getTestByUUID(String uuid) {
        // Отримуємо тест по UUID
        Test test = testDao.getByUUID(uuid);
        if (test == null) throw new ProgPlatformNotFoundException(ErrorConsts.TEST_NOT_FOUND);
        return testGetTestResponseConverter.convert(test);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAllTestResponse getTestByTopicId(Integer topicId) {
        List<TestView> test = testDao.getByTopicId(topicId).stream()
                .map(testTestViewConverter::convert)
                .toList();
        return new GetAllTestResponse(test);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CheckTestAnswersResultResponse check(CheckTestAnswersRequest request) {
        // Отримуємо тест по UUID
        Test test = testDao.getByUUID(request.getUuid());
        if (test == null) throw new ProgPlatformNotFoundException(ErrorConsts.TEST_NOT_FOUND);
        // Отримуємо запитання та відповіді з бази
        List<Question> originalQuestions = test.getQuestions();
        int maxAssessment = originalQuestions.size();
        // Зберігаємо правильні та неправильні відповіді
        Map<Boolean, List<AnswerView>> checkedAnswers = request.getAnswers()
                .stream()
                // Збираємо правильні та неправильні відповіді (true - правильні відповіді, false - неправильні)
                .collect(Collectors.partitioningBy(new CheckAnswers(originalQuestions)));
        // Отримуємо всі правильні відповіді
        List<AnswerView> correctAnswers = checkedAnswers.get(true);
        // Отримуємо кількість правильних відповідей
        int currentAssessment = correctAnswers.size();
        // Отримуємо всі неправильні відповіді
        List<Question> wrongAnswers = checkedAnswers.get(false).stream()
                .map(new AnswerViewToQuestion(originalQuestions))
                .toList();
        Date created = new Date();
        Gson gson = new Gson();
        // Зберігаємо результат тестування
        TestResult testResult = TestResult.builder()
                .userId(request.getUserId())
                .testUuid(request.getUuid())
                .maxAssessment(maxAssessment)
                .assessment(currentAssessment)
                .correct(gson.toJson(correctAnswers))
                .incorrect(gson.toJson(wrongAnswers))
                .created(created)
                .build();
        testResultDao.create(testResult);
        // Формуємо відповідь API
        return new CheckTestAnswersResultResponse(maxAssessment, currentAssessment, correctAnswers, wrongAnswers, created);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestResultsResponse getTestResultByUserId(Integer userId) {
        List<TestResult> results = testResultDao.getTestResultsByUserId(userId);
        return testResultsResponseTestResultsConverter.convert(results);
    }
}
