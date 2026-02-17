package org.ua.fkrkm.progplatform.filter.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.ua.fkrkm.progplatform.filter.RateLimitingFilter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class RateLimitingFilterConfig {

    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter() {
        FilterRegistrationBean<RateLimitingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitingFilter(this.requestCounts));
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Scheduled(fixedRate = 60_000)
    public void clearCounts() {
        this.requestCounts.clear();
        System.out.println("Counts cleared!");
    }
}
