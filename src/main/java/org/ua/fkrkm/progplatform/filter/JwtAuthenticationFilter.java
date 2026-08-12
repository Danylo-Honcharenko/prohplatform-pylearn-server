package org.ua.fkrkm.progplatform.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.ua.fkrkm.proglatformdao.dao.AuthDaoI;
import org.ua.fkrkm.proglatformdao.entity.Auth;
import org.ua.fkrkm.progplatform.exceptions.InvalidJwtAuthenticationException;
import org.ua.fkrkm.progplatform.exceptions.RevokedJwtAuthenticationException;
import org.ua.fkrkm.progplatform.services.JwtServiceI;
import org.ua.fkrkm.progplatformclientlib.response.ErrorResponse;
import org.ua.fkrkm.progplatformclientlib.response.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    // Назва cookie
    private final String cookiesTokenName;
    // DAO для роботи з аунтифікованими користувачами
    private final AuthDaoI authDao;
    // Логер
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final ObjectMapper objectMapper;

    private final RequestMatcher skipJwtAuthentication = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/user/login", "POST"),
            new AntPathRequestMatcher("/api/user/registration", "POST"),
            new AntPathRequestMatcher("/api/user/updatePassword", "PUT")
    );

    /**
     * Конструктор
     *
     * @param jwtService Сервіс для роботи з Jwt токеном
     * @param userDetailsService Сервіс з деталями про користувача
     * @param cookiesTokenName Назва cookies в якому може зберігатися токен
     * @param authDao DAO для роботи з аунтифікованими користувачами
     */
    public JwtAuthenticationFilter(JwtServiceI jwtService,
                                   UserDetailsService userDetailsService,
                                   @Value("${cookies.jwt.token.name}") String cookiesTokenName,
                                   AuthDaoI authDao,
                                   ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.cookiesTokenName = cookiesTokenName;
        this.authDao = authDao;
        this.objectMapper = objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return skipJwtAuthentication.matches(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = this.getJwtToken(request);

            if (token != null) {
                this.authenticateToken(token, request);
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException exception) {

            this.logError(exception);
            this.processError(response, "Токен авторизації не дійсний", "Виконайте вхід у систему");

        } catch (UsernameNotFoundException exception) {

            this.logError(exception);
            this.processError(response, "Не знайдено користувача", "Не вдалось знайти користувача при спробі авторизації");

        } catch (JwtException | IllegalArgumentException exception) {

            this.logError(exception);
            this.processError(response, "Токен авторизації не валідний", "Виконайте вхід у систему");

        }
    }

    /**
     * Отримуємо токен з хедеру або з cookie
     *
     * @param request запит
     * @return String токен
     */
    private String getJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            return token.isEmpty() ? null : token;
        }

        return this.getJwtTokenFromCookie(request);
    }

    /**
     * Отримати токен з cookies
     *
     * @param request запит
     * @return String токен
     */
    private String getJwtTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookiesTokenName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse(null);
    }

    /**
     * Аутентифікуємо користувача по токену
     *
     * @param token токен
     * @param request запит
     */
    private void authenticateToken(String token, HttpServletRequest request) {
        String email = jwtService.extractUserName(token);

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("JWT subject is missing");
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        UserDetails user = userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(token, user)) {
            throw new InvalidJwtAuthenticationException("JWT claims are invalid");
        }

        if (!checkAccessTokenInDatabase(token)) {
            throw new RevokedJwtAuthenticationException("JWT is revoked");
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

        authentication.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Обробка помилки
     *
     * @param response відповідь
     * @param message повідомлення помилки
     * @param detail опис помилки
     * @throws IOException помилка
     */
    private void processError(HttpServletResponse response, String message, String detail) throws IOException {

        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Response<ErrorResponse> error = new Response<>(
                HttpStatus.UNAUTHORIZED,
                new ErrorResponse(
                        message,
                        detail,
                        MDC.get("msid")
                )
        );

        this.objectMapper.writeValue(response.getOutputStream(), error);
    }

    /**
     * Логуємо помилку
     *
     * @param exception помилка
     */
    private void logError(RuntimeException exception) {
        LOGGER.error("Error: {}, Trace UUID: {}", exception.getMessage(), MDC.get("msid"));
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
