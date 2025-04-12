package org.ua.fkrkm.progplatform.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.ua.fkrkm.progplatform.utils.Msid;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Фільтр для логування
 */
@Component
@Order(1)
public class LogFilter implements Filter {

    // Логер
    private final static Logger LOGGER = LoggerFactory.getLogger(LogFilter.class);
    // Поточна дата
    private final static String DATE = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date());

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        // Отримуємо MSID
        String msid = req.getHeader("MSID");
        // Перевіряємо що він присутній
        if (msid == null || msid.isBlank()) {
            // Генеруємо UUID
            UUID uuid = UUID.randomUUID();
            LOGGER.info("Request: {}, Time: {}, MSID: {}", req.getRequestURI(), DATE, uuid);
            // Встановлюємо UUID запиту
            Msid.set(uuid);
            // Додаємо до відповіді
            res.addHeader("MSID", String.valueOf(uuid));
            filterChain.doFilter(req, res);
            return;
        }
        LOGGER.info("Request: {}, Time: {}, MSID: {}", req.getRequestURI(), DATE, msid);
        // Встановлюємо UUID запиту
        Msid.set(msid);
        // Додаємо до відповіді
        res.addHeader("MSID", msid);
        filterChain.doFilter(req, res);
    }
}
