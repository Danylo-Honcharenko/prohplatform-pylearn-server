package org.ua.fkrkm.progplatform.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(-105)
public class LoggerFilter extends OncePerRequestFilter {

    // Логер
    private final static Logger LOGGER = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Генеруємо UUID
            UUID uuid = UUID.randomUUID();
            LOGGER.info("Request: {}, MSID: {}", request.getRequestURI(), uuid);
            // Встановлюємо UUID запиту
            MDC.put("msid", uuid.toString());
            // Додаємо до відповіді
            response.addHeader("MSID", String.valueOf(uuid));

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
