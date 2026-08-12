package org.ua.fkrkm.progplatform.controllers.error;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformExceptionBadRequest;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformNotFoundException;
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
        LOGGER.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), ex.getMessage(), ex);
        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorResponse(
                "Помилка серверу", "Помилка серверу. Спробуйте ще раз", MDC.get("msid")
        ));
    }

    /**
     * Помилка
     *
     * @param ex помилка
     * @return Response<ErrorResponse> відповідь API
     */
    @ExceptionHandler(value = ProgPlatformException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<ErrorResponse> errorProg(ProgPlatformException ex) {
        LOGGER.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), ex.getMessage(), ex);
        return new Response<>(HttpStatus.BAD_REQUEST, new ErrorResponse(
                "Помилка", ex.getMessage(), MDC.get("msid")
        ));
    }

    /**
     * Помилка
     *
     * @param ex помилка
     * @return Response<ErrorResponse> відповідь API
     */
    @ExceptionHandler(value = ProgPlatformExceptionBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<ErrorResponse> errorProg(ProgPlatformExceptionBadRequest ex) {
        LOGGER.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), ex.getMessage(), ex);
        return new Response<>(HttpStatus.BAD_REQUEST, new ErrorResponse(
                "Помилка", ex.getMessage(), MDC.get("msid")
        ));
    }

    /**
     * Не знайдено
     *
     * @param ex помилка
     * @return Response<ErrorResponse> відповідь API
     */
    @ExceptionHandler(value = ProgPlatformNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<ErrorResponse> errorProgNotFound(ProgPlatformNotFoundException ex) {
        LOGGER.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), ex.getMessage(), ex);
        return new Response<>(HttpStatus.NOT_FOUND, new ErrorResponse(
                "Не знайдено об'єкт", ex.getMessage(), MDC.get("msid")
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
        LOGGER.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), e.getMessage(), e);
        return new Response<>(HttpStatus.FORBIDDEN, new ErrorResponse(
                "Відмовлено в доступі", "У вас не має доступу до ресурсу", MDC.get("msid")
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
                "Помилка валідції вхідних параметрів", errors
        ));
    }
}
