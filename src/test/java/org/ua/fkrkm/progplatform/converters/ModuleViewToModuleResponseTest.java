package org.ua.fkrkm.progplatform.converters;

import org.junit.jupiter.api.Test;
import org.ua.fkrkm.proglatformdao.entity.Module;
import org.ua.fkrkm.proglatformdao.entity.view.ModuleView;
import org.ua.fkrkm.progplatformclientlib.response.ModuleResponse;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ModuleViewToModuleResponseTest {

    @Test
    public void moduleViewPreservesTimestamps() {
        Date created = new Date(1_000L);
        Date updated = new Date(2_000L);
        Module module = Module.builder()
                .id(7L)
                .name("Модуль")
                .description("Опис")
                .created(created)
                .updated(updated)
                .build();

        ModuleView view = new ModuleToModuleView().convert(module);

        assertNotNull(view);
        assertEquals(created, view.getCreated());
        assertEquals(updated, view.getUpdated());
    }

    @Test
    public void convertPreservesModuleData() {
        Date created = new Date(1_000L);
        Date updated = new Date(2_000L);
        ModuleView source = ModuleView.builder()
                .id(7L)
                .name("Модуль")
                .description("Опис")
                .complete(new BigDecimal("42.5"))
                .active(true)
                .created(created)
                .updated(updated)
                .build();

        ModuleResponse response = new ModuleViewToModuleResponse().convert(source);

        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(source.getId(), response.getData().getId());
        assertEquals(source.getName(), response.getData().getName());
        assertEquals(source.getDescription(), response.getData().getDescription());
        assertEquals(source.getComplete(), response.getData().getComplete());
        assertEquals(source.getActive(), response.getData().getActive());
        assertEquals(created, response.getData().getCreated());
        assertEquals(updated, response.getData().getUpdated());
    }
}
