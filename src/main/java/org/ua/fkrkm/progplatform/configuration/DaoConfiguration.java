package org.ua.fkrkm.progplatform.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.ua.fkrkm.proglatformdao.dao.*;
import org.ua.fkrkm.proglatformdao.dao.impl.*;

import javax.sql.DataSource;

/**
 * Конфігурація DAO
 */
@Configuration
public class DaoConfiguration {

    /**
     * DAO користувачів
     *
     * @param dataSource джерело бази даних
     * @return UserDaoI інтерфейс DAO користувачів
     */
    @Bean
    public UserDaoI getUserDao(@Qualifier("databaseProg") DataSource dataSource) {
        return new UserDaoImpl(dataSource);
    }

    /**
     * DAO ролів
     *
     * @param dataSource джерело бази даних
     * @return UserDaoI інтерфейс DAO ролів
     */
    @Bean
    public RoleDaoI getRoleDao(@Qualifier("databaseProg") DataSource dataSource) {
        return new RoleDaoImpl(dataSource);
    }

    /**
     * DAO курсів
     *
     * @param dataSource джерело бази даних
     * @return CourseDaoI інтерфейс DAO курсів
     */
    @Bean
    public CourseDaoI getCourseDao(@Qualifier("databaseProg") DataSource dataSource) {
        return new CourseDaoImpl(dataSource);
    }

    /**
     * DAO тем курсів
     *
     * @param dataSource джерело бази даних
     * @return TopicDaoI інтерфейс DAO тем курсів
     */
    @Bean
    public TopicDaoI getTopicDao(@Qualifier("databaseProg") DataSource dataSource) {
        return new TopicDaoImpl(dataSource);
    }

    /**
     * DAO завдань
     *
     * @param dataSource джерело бази даних
     * @return ExerciseDaoI інтерфейс DAO завдань
     */
    @Bean
    public ExerciseDaoI getExerciseDao(@Qualifier("databaseProg") DataSource dataSource) {
        return new ExerciseDaoImpl(dataSource);
    }

    /**
     * DAO збережених результатів тестування
     *
     * @param dataSource джерело бази даних
     * @return TestResultDaoI інтерфейс DAO результатів тестування
     */
    @Bean
    public TestResultDaoI getTestResultDao(@Qualifier("databaseProg") DataSource dataSource) {
        return new TestResultDaoImpl(dataSource);
    }

    /**
     * DAO модулі курсу
     *
     * @param dataSource джерело бази даних
     * @return TestResultDaoI інтерфейс DAO для роботи з модулями курсу
     */
    @Bean
    public ModuleDaoI getModuleDao(@Qualifier("databaseProg") DataSource dataSource) {
        return new ModuleDaoImpl(dataSource);
    }

    /**
     * DAO статистики по модулю
     *
     * @param dataSource джерело бази даних
     * @return ModuleStatDaoI інтерфейс DAO для роботи зі статистикой по модулю
     */
    @Bean
    public ModuleStatDaoI getModuleStatDao(@Qualifier("databaseProg") DataSource dataSource) {
        return new ModuleStatImpl(dataSource);
    }

    /**
     * DAO для перевірки аунтифікованих користувачів
     *
     * @param dataSource джерело бази даних
     * @return інтерфейс DAO для роботи з аунтифікованими користувачами
     */
    @Bean
    public AuthDaoI getAuthDao(@Qualifier("databaseProg") DataSource dataSource) {
        return new AuthDaoImpl(dataSource);
    }

    //=============================MongoDB===================================

    /**
     * DAO тестів
     *
     * @param mongoDatabaseFactory джерело підключення до MongoDB
     * @return TestDaoI інтерфейс DAO тестів
     */
    @Bean
    public TestDaoI getTestDao(@Qualifier("mongoConnectFactory") MongoDatabaseFactory mongoDatabaseFactory) {
        return new TestDaoImpl(mongoDatabaseFactory);
    }
}
