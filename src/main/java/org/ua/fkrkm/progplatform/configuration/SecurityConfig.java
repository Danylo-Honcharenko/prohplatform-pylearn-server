package org.ua.fkrkm.progplatform.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.ua.fkrkm.progplatform.filter.JwtAuthenticationFilter;
import org.ua.fkrkm.progplatform.filter.config.RestAuthenticationEntryPoint;

/**
 * Конфігурація безпеки
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfig corsConfig;

    private static final String[] PERMIT_ALL = {
            "/",
            "/swagger-ui/**",
            "/v3/api-docs*/**",
            "/api/user/registration",
            "/api/user/login",
            "/api/course/**",
            "/api/test/**",
            "/api/topic/**",
            "/api/module/**"
    };

    private static final String[] PERMIT_ADMIN = {
            "/api/admin/user/**",
            "/api/admin/role/**"
    };

    private static final String[] PERMIT_TEACHER_AND_ADMIN = {
            "/api/admin/course/**",
            "/api/admin/topic/**",
            "/api/admin/test/**"
    };

    /**
     * Фільтр запитів
     *
     * @param http запит
     * @return SecurityFilterChain інтерфейс фільтру
     * @throws Exception помилка
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        (auth) -> auth
                                .requestMatchers(PERMIT_ALL).permitAll()
                                .requestMatchers(PERMIT_ADMIN).hasRole("ADMIN")
                                .requestMatchers(PERMIT_TEACHER_AND_ADMIN).hasAnyRole("ADMIN", "TEACHER")
                                .anyRequest()
                                .authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfig.getCorsConfigSource()))
                .authenticationProvider(getAuthenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                .build();
    }

    /**
     * Шифрувальник паролів
     *
     * @return PasswordEncoder шифрувальник паролів
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Менеджер аутентифікації
     *
     * @param config конфігурація аутентифікації
     * @return AuthenticationManager менеджер аутентифікації
     * @throws Exception помилка
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Провайдер аутентифікації
     *
     * @return AuthenticationProvider провайдер аутентифікації
     */
    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(getPasswordEncoder());
        return provider;
    }
}
