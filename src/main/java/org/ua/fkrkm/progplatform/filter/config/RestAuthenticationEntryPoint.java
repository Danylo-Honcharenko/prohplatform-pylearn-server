package org.ua.fkrkm.progplatform.filter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.progplatformclientlib.response.ErrorResponse;
import org.ua.fkrkm.progplatformclientlib.response.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Response<ErrorResponse> error = new Response<>(
                HttpStatus.UNAUTHORIZED,
                new ErrorResponse(
                        "Неавторизовано",
                        "Потрібно виконати вхід у систему",
                        MDC.get("msid")
                )
        );

        this.objectMapper.writeValue(response.getOutputStream(), error);
    }
}
