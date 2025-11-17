package org.ua.fkrkm.progplatform.filter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.ua.fkrkm.progplatform.utils.Msid;
import org.ua.fkrkm.progplatformclientlib.response.ErrorResponse;
import org.ua.fkrkm.progplatformclientlib.response.Response;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getOutputStream().println(objectMapper.writeValueAsString(new Response<>(HttpStatus.FORBIDDEN, new ErrorResponse(
                "Access denied!", "You do not have sufficient rights to perform this action!", Msid.get()
        ))));
    }
}
