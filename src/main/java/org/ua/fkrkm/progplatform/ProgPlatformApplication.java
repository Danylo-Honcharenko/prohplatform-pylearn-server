package org.ua.fkrkm.progplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProgPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProgPlatformApplication.class, args);
    }
}
