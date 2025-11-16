package org.ua.fkrkm.progplatform.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Конфігурація підключення бази даних
 */
@Configuration
public class DatabaseConfiguration {

    @Value("${db.driver}")
    private String driverClassName;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;

    /**
     * Отримати джерело бази даних
     *
     * @return DataSource — джерело бази даних
     */
    @Bean("databaseProg")
    public DataSource getDataSource(@Value("${spring.profiles.active:}") String activeProfile) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        // Зберегти дані зі скрипта в тестову базу H2
        this.saveDataToH2(activeProfile, dataSource);
        return dataSource;
    }

    /**
     * Зберегти дані зі скрипта в тестову базу H2
     *
     * @param activeProfile поточний активний профіль spring
     * @param dataSource джерело бази даних
     */
    private void saveDataToH2(String activeProfile, DataSource dataSource) {
        Consumer<String> setTestData = (profile) -> {
            try {
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                populator.addScript(new ClassPathResource("db.sql"));
                populator.execute(dataSource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Arrays.stream(activeProfile.split(","))
                .filter((profile) -> profile.equals("test"))
                .findFirst()
                .ifPresent(setTestData);
    }
}
