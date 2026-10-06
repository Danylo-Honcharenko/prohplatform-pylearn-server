package org.ua.fkrkm.progplatform.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ua.fkrkm.progplatform.services.impl.TestServiceImpl;
import org.ua.fkrkm.progplatformclientlib.request.CheckTestAnswersRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestControllerTest {

    private TestController testController;
    private TestServiceImpl testService;

    @BeforeEach
    public void setUp() {
        this.testService = mock(TestServiceImpl.class);
        this.testController = new TestController(testService);
    }

    @Test
    public void checkTest() {
        CheckTestAnswersRequest request = new CheckTestAnswersRequest();
        testController.check(request);
        verify(testService).check(request);
    }

    @Test
    public void getTestByUUID() {
        testController.getTestByUUID("uuid");
        verify(testService).getTestByUUID("uuid");
    }

    @Test
    public void getAllTest() {
        testController.getAll();
        verify(testService).getAll();
    }

    @Test
    public void getTestResultByUserIdTest() {
        testController.getAllUserTestResult();
        verify(testService).getAllUserTestResult();
    }
}
