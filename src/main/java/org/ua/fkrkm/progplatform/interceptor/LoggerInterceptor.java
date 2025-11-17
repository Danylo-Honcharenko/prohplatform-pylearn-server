package org.ua.fkrkm.progplatform.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ua.fkrkm.progplatform.utils.Msid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class LoggerInterceptor implements HandlerInterceptor {

    // Логер
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LoggerInterceptor.class);
    // Поточна дата
    private final static String DATE = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Отримуємо MSID
        String msid = request.getHeader("MSID");
        // Перевіряємо що він присутній
        if (msid == null || msid.isBlank()) {
            // Генеруємо UUID
            UUID uuid = UUID.randomUUID();
            LOGGER.info("Request: {}, Time: {}, MSID: {}", request.getRequestURI(), DATE, uuid);
            // Встановлюємо UUID запиту
            Msid.set(uuid);
            MDC.put("msid", uuid.toString());
            // Додаємо до відповіді
            response.addHeader("MSID", String.valueOf(uuid));
            return true;
        }
        LOGGER.info("Request: {}, Time: {}, MSID: {}", request.getRequestURI(), DATE, msid);
        // Встановлюємо UUID запиту
        Msid.set(msid);
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
