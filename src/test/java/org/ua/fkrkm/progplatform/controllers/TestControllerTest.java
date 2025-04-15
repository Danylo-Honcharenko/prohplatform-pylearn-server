package org.ua.fkrkm.progplatform.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ua.fkrkm.progplatform.services.impl.TestServiceImpl;
import org.ua.fkrkm.progplatformclientlib.request.CheckTestAnswersRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class TestControllerTest {

    private TestController testController;

    @BeforeEach
    public void setUp() {
        this.testController = new TestController(mock(TestServiceImpl.class));
    }

    @Test
    public void checkTest() {
        assertNotNull(testController.check(new CheckTestAnswersRequest()));
    }

    @Test
    public void getTestByUUID() {
        assertNotNull(testController.getTestByUUID("uuid"));
    }

    @Test
    public void getAllTest() {
        assertNotNull(testController.getAll());
    }

    @Test
    public void getTestResultByUserIdTest() {
        assertNotNull(testController.getTestResultByUserId(1));
    }
}
