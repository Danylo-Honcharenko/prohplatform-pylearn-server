package org.ua.fkrkm.progplatform.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ua.fkrkm.progplatform.services.impl.TopicServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class TopicControllerTest {

    private TopicController topicController;

    @BeforeEach
    public void setUp() {
        this.topicController = new TopicController(mock(TopicServiceImpl.class));
    }

    @Test
    public void getAllModuleTopicsTest() {
        assertNotNull(topicController.getAllModuleTopics(1));
    }

    @Test
    public void getTopicByIdTest() {
        assertNotNull(topicController.getTopicById(1));
    }
}
