package org.ua.fkrkm.progplatform.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ua.fkrkm.progplatform.services.impl.TopicServiceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TopicControllerTest {

    private TopicController topicController;
    private TopicServiceImpl topicService;

    @BeforeEach
    public void setUp() {
        this.topicService = mock(TopicServiceImpl.class);
        this.topicController = new TopicController(topicService);
    }

    @Test
    public void getAllModuleTopicsTest() {
        topicController.getAllModuleTopics(1L);
        verify(topicService).getAllModuleTopics(1L);
    }

    @Test
    public void getTopicByIdTest() {
        topicController.getTopicById(1L);
        verify(topicService).getTopicById(1L);
    }
}
