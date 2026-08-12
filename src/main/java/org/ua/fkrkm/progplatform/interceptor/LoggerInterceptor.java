package org.ua.fkrkm.progplatform.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class LoggerInterceptor implements HandlerInterceptor {

    // Логер
    private final static Logger LOGGER = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Отримуємо MSID
        String msid = request.getHeader("MSID");
        // Перевіряємо що він присутній
        if (msid == null || msid.isBlank()) {
            // Генеруємо UUID
            UUID uuid = UUID.randomUUID();
            LOGGER.info("Request: {}, MSID: {}", request.getRequestURI(), uuid);
            // Встановлюємо UUID запиту
            MDC.put("msid", uuid.toString());
            // Додаємо до відповіді
            response.addHeader("MSID", String.valueOf(uuid));
            return true;
        }
        LOGGER.info("Request: {}, MSID: {}", request.getRequestURI(), msid);
        // Встановлюємо UUID запиту
        MDC.put("msid", msid);
        // Додаємо до відповіді
        response.addHeader("MSID", msid);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        MDC.clear();
    }
}
