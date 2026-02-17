package org.ua.fkrkm.progplatform.filter;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitingFilter implements Filter {

    private final static Logger LOGGER = LoggerFactory.getLogger(RateLimitingFilter.class);

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private final Map<String, AtomicInteger> requestCounts;

    public RateLimitingFilter(Map<String, AtomicInteger> requestCounts) {
        this.requestCounts = requestCounts;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String clientIp = httpRequest.getRemoteAddr();
        requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));
        int currentCount = requestCounts.get(clientIp).incrementAndGet();

        if (currentCount > MAX_REQUESTS_PER_MINUTE) {
            LOGGER.warn("Too many requests from IP: {}", clientIp);
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write(new Gson().toJson(Map.of("message", "Too many requests!")));
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
