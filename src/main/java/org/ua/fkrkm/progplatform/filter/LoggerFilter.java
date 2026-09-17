package org.ua.fkrkm.progplatform.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(-105)
public class LoggerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Генеруємо UUID
            UUID uuid = UUID.randomUUID();
            log.info("Request: {}, MSID: {}", request.getRequestURI(), uuid);
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
