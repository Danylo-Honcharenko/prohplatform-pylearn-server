package org.ua.fkrkm.progplatform.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ua.fkrkm.progplatform.services.impl.ModuleServiceImpl;
import org.ua.fkrkm.progplatformclientlib.request.SetModuleTopicCompletedRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class ModuleControllerTest {

    private ModuleController moduleController;

    @BeforeEach
    public void setUp() {
        this.moduleController = new ModuleController(mock(ModuleServiceImpl.class));
    }

    @Test
    public void setCompletedModuleTopicTest() {
        assertNotNull(moduleController.setCompletedModuleTopic(new SetModuleTopicCompletedRequest()));
    }
}
