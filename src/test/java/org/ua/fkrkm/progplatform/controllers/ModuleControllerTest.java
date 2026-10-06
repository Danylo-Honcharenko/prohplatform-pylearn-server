package org.ua.fkrkm.progplatform.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ua.fkrkm.progplatform.services.impl.ModuleServiceImpl;
import org.ua.fkrkm.progplatformclientlib.request.SetModuleTopicCompletedRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ModuleControllerTest {

    private ModuleController moduleController;
    private ModuleServiceImpl moduleService;

    @BeforeEach
    public void setUp() {
        this.moduleService = mock(ModuleServiceImpl.class);
        this.moduleController = new ModuleController(moduleService);
    }

    @Test
    public void setCompletedModuleTopicTest() {
        SetModuleTopicCompletedRequest request = new SetModuleTopicCompletedRequest();
        moduleController.setCompletedModuleTopic(request);
        verify(moduleService).setCompletedModuleTopic(request);
    }
}
