package org.ua.fkrkm.progplatform.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфігурація Swagger
 */
@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfig {

    /**
     * Отримати конфігурацію OpenAPI (Swagger)
     *
     * @return OpenAPI конфігурація
     */
    @Bean
    public OpenAPI getOpenAPIConfig() {
        Info info = new Info();
        info.setTitle("ProgPlatform (PyLearn)");
        info.setVersion("0.0.1-SNAPSHOT");
        info.setDescription("Бекенд частина платформи для навчання мови програмування");
        return new OpenAPI().info(info);
    }
}
