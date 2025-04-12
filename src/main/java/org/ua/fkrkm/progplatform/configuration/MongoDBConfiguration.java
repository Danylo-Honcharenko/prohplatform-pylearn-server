package org.ua.fkrkm.progplatform.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * Конфігурація підключення до MongoDB
 */
@Configuration
public class MongoDBConfiguration {

    @Value("${mongo.connect.url}")
    private String connectURL;

    /**
     * Отримати підключення до MongoDB
     *
     * @return
     */
    @Bean("mongoConnectFactory")
    public MongoDatabaseFactory getMongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(MongoClients.create(connectURL), "progplatfrom");
    }
}
