package org.ua.fkrkm.progplatform.services;

import org.ua.fkrkm.progplatformclientlib.request.*;
import org.ua.fkrkm.progplatformclientlib.response.*;

/**
 * Інтерфейс для роботи з тестами
 */
public interface TestServiceI {
    /**
     * Створити тест
     *
     * @param request запит
     * @return CreateTestResponse відповідь API
     */
    CreateTestResponse create(CreateTestRequest request);
    /**
     * Отримати всі тести
     *
     * @return GetAllTestResponse відповідь API
     */
    GetAllTestResponse getAll();
    /**
     * Отримати тест по UUID
     *
     * @param uuid UUID тесту
     * @return GetTestResponse відповідь API
     */
    GetTestResponse getTestByUUID(String uuid);
    /**
     * Отримати тести по ID теми
     *
     * @param topicId ID теми
     * @return GetAllTestResponse відповідь API
     */
    GetAllTestResponse getTestByTopicId(Integer topicId);
    /**
     * Перевірити тест
     *
     * @param request запит
     * @return CheckTestAnswersResultResponse відповідь API
     */
    CheckTestAnswersResultResponse check(CheckTestAnswersRequest request);
    /**
     * Отримати результати тестування по ID користувача
     *
     * @param userId ID користувача
     * @return TestResultsResponse відповідь API
     */
    TestResultsResponse getTestResultByUserId(Integer userId);
}
