package org.ua.fkrkm.progplatform.controllers.error;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.ua.fkrkm.progplatform.utils.Msid;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

import java.util.HashMap;
import java.util.Map;

/**
 * Клас можливих помилок API
 */
@RestControllerAdvice
public class ErrorHandleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ErrorHandleController.class);

    /**
     * Помилка серверу
     *
     * @param ex помилка
     * @return Response<ErrorResponse> відповідь API
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<ErrorResponse> error(Exception ex) {
        LOGGER.error("MSID: {}, Повідомлення помилки: {}", Msid.get(), ex.getMessage(), ex);
        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorResponse(
                "Server error", ex.getMessage(), Msid.get()
        ));
    }

    /**
     * Помилка
     *
     * @param ex помилка
     * @return Response<ErrorResponse> відповідь API
     */
    @ExceptionHandler(value = ProgPlatformException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<ErrorResponse> errorProg(ProgPlatformException ex) {
        LOGGER.error("MSID: {}, Повідомлення помилки: {}", Msid.get(), ex.getMessage(), ex);
        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorResponse(
                "Error", ex.getMessage(), Msid.get()
        ));
    }

    /**
     * Помилка валідації JWT токену
     *
     * @param e помилка
     * @return Response<ErrorResponse> відповідь API
     */
    @ExceptionHandler(value = ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response<ErrorResponse> jwtError(ExpiredJwtException e) {
        LOGGER.info(e.getMessage());
        return new Response<>(HttpStatus.UNAUTHORIZED, new ErrorResponse(
                "Invalid Jwt token", e.getMessage(), Msid.get()
        ));
    }

    /**
     * Помилка сігнатури JWT токену
     *
     * @param e помилка
     * @return Response<ErrorResponse> відповідь API
     */
    @ExceptionHandler(value = SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response<ErrorResponse> jwtErrorSig(SignatureException e) {
        LOGGER.info(e.getMessage());
        return new Response<>(HttpStatus.UNAUTHORIZED, new ErrorResponse(
                "Jwt token signature incorrect", e.getMessage(), Msid.get()
        ));
    }

    /**
     * Помилка відмови в доступі
     *
     * @param e помилка
     * @return Response<ErrorResponse> відповідь API
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response<ErrorResponse> accessDenied(AccessDeniedException e) {
        LOGGER.error("MSID: {}, Повідомлення помилки: {}", Msid.get(), e.getMessage(), e);
        return new Response<>(HttpStatus.FORBIDDEN, new ErrorResponse(
                "Access denied", e.getMessage(), Msid.get()
        ));
    }

    /**
     * Помилка валідації вхідних параметрів
     *
     * @param ex помилка
     * @return Response<FieldValidResponse> відповідь API
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<FieldValidResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new Response<>(HttpStatus.BAD_REQUEST, new FieldValidResponse(
                "Input parameter validation error", errors
        ));
    }
}
