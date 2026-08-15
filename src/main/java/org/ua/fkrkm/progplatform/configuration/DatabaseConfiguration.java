package org.ua.fkrkm.progplatform.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
/**
 * Конфігурація підключення бази даних
 */
@Configuration
public class DatabaseConfiguration {

//    @Value("${db.driver}")
//    private String driverClassName;
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
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(this.url);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);

        hikariConfig.setPoolName("progrplatform-postgres");
        hikariConfig.setMaximumPoolSize(16);
        hikariConfig.setMinimumIdle(16);
        hikariConfig.setConnectionTimeout(5_000);
        hikariConfig.setValidationTimeout(2_000);
        hikariConfig.setMaxLifetime(25 * 60_000);
        hikariConfig.setKeepaliveTime(2 * 60_000);
        hikariConfig.setAutoCommit(true);

        return new HikariDataSource(hikariConfig);
    }
}
