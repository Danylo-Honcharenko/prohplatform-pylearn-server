package org.ua.fkrkm.progplatform.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.ua.fkrkm.proglatformdao.dao.AuthDaoI;
import org.ua.fkrkm.proglatformdao.entity.Auth;
import org.ua.fkrkm.progplatform.services.JwtServiceI;
import org.ua.fkrkm.progplatform.utils.Msid;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Фільтр для аутентифікації запиту
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Сервіс для роботи з Jwt токеном
    private final JwtServiceI jwtService;
    // Сервіс з деталями про користувача
    private final UserDetailsService userDetailsService;
    // Обробка помилок
    private final HandlerExceptionResolver handlerExceptionResolver;
    // Назва cookie
    private final String cookiesTokenName;
    // DAO для роботи з аунтифікованими користувачами
    private final AuthDaoI authDao;
    // Логер
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    // Поточна дата
    private final static String DATE = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date());
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private static final String[] NOT_FILTERED_URLS = {
            "/progplatform/",
            "/**/api/user/registration",
            "/**/api/user/login",
            "/**/api/course/getAll",
            "/**/swagger-ui/**",
            "/**/v3/api-docs/**"
    };

    /**
     * Конструктор
     *
     * @param jwtService Сервіс для роботи з Jwt токеном
     * @param userDetailsService Сервіс з деталями про користувача
     * @param handlerExceptionResolver Обробка помилок
     * @param cookiesTokenName Назва cookies в якому може зберігатися токен
     * @param authDao DAO для роботи з аунтифікованими користувачами
     */
    public JwtAuthenticationFilter(JwtServiceI jwtService,
                                   UserDetailsService userDetailsService,
                                   HandlerExceptionResolver handlerExceptionResolver,
                                   @Value("${cookies.jwt.token.name}") String cookiesTokenName,
                                   AuthDaoI authDao) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.cookiesTokenName = cookiesTokenName;
        this.authDao = authDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(NOT_FILTERED_URLS)
                .anyMatch(uri -> this.antPathMatcher.match(uri, request.getRequestURI()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Намагаємось отримати із запиту повний рядок разом з токеном
        String authHeader = request.getHeader("Authorization");
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // Намагаємось отрмати токен з cookies
                String jwtTokenFromCookie = getJwtTokenFromCookie(request.getCookies());
                if (jwtTokenFromCookie != null) {
                    LOGGER.info("Get token from cookie. Time: {}, MSID: {}", DATE, Msid.get());
                    // Обробляємо токен
                    processJwtToken(jwtTokenFromCookie, request);
                }
                filterChain.doFilter(request, response);
                return;
            }
            // Прибираємо Bearer в рядку з токеном
            String jwt = authHeader.substring(7);
            LOGGER.info("Get token from header. Time: {}, MSID: {}", DATE, Msid.get());
            // Обробляємо токен
            processJwtToken(jwt, request);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    /**
     * Отримати токен з cookies
     *
     * @param cookies cookies
     * @return String токен
     */
    private String getJwtTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookiesTokenName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Обробка токену
     *
     * @param token токен
     * @param request запит
     */
    private void processJwtToken(String token, HttpServletRequest request) {
        // З токену отримуємо Email користувача
        String email = jwtService.extractUserName(token);
        // З SecurityContextHolder отримуємо поточний стан аутентифікації
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Перевіряємо що ми отримали Email
        if (email != null && authentication == null) {
            // Отримуємо користувача по email з бази даних
            UserDetails user = userDetailsService.loadUserByUsername(email);
            // Перевіряємо токен на валідність
            if (jwtService.isTokenValid(token, user) && checkAccessTokenInDatabase(token)) {
                LOGGER.info("Token valid. Time: {}, MSID: {}", DATE, Msid.get());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
    }

    /**
     * Перевіряємо наявність активного токена в базі
     *
     * @param accessToken токен
     * @return boolean наявність true/false
     */
    private boolean checkAccessTokenInDatabase(String accessToken) {
        List<Auth> auth = authDao.getByAccessToken(accessToken);
        return !auth.isEmpty();
    }
}
