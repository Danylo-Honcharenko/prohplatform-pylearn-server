package org.ua.fkrkm.progplatform.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

/**
 * Конфігурація локалізації
 */
@Configuration
public class LocalConfig {

    /**
     * Конфігурація локалізації
     *
     * @return LocaleResolver резолвер
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.of("en"));
        localeResolver.setSupportedLocales(List.of(
                Locale.of("en"),
                Locale.of("ru"),
                Locale.of("uk")
        ));
        return localeResolver;
    }
}
