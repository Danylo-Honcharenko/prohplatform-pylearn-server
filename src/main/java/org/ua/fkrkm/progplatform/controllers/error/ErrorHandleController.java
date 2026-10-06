package org.ua.fkrkm.progplatform.controllers.error;

import lombok.extern.slf4j.Slf4j;
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
import org.ua.fkrkm.progplatformclientlib.data.ErrorData;
import org.ua.fkrkm.progplatformclientlib.data.FieldValidData;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

import java.util.HashMap;
import java.util.Map;

/**
 * Клас можливих помилок API
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandleController {

    /**
     * Помилка серверу
     *
     * @param ex помилка
     * @return ErrorResponse відповідь API
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse error(Exception ex) {
        log.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), ex.getMessage(), ex);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorData(
                "Помилка серверу", "Помилка серверу. Спробуйте ще раз", MDC.get("msid")
        ));
    }

    /**
     * Помилка
     *
     * @param ex помилка
     * @return ErrorResponse відповідь API
     */
    @ExceptionHandler(value = ProgPlatformException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse errorProg(ProgPlatformException ex) {
        log.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), ex.getMessage(), ex);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, new ErrorData(
                "Помилка", ex.getMessage(), MDC.get("msid")
        ));
    }

    /**
     * Помилка
     *
     * @param ex помилка
     * @return ErrorResponse відповідь API
     */
    @ExceptionHandler(value = ProgPlatformExceptionBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse errorProg(ProgPlatformExceptionBadRequest ex) {
        log.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), ex.getMessage(), ex);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, new ErrorData(
                "Помилка", ex.getMessage(), MDC.get("msid")
        ));
    }

    /**
     * Не знайдено
     *
     * @param ex помилка
     * @return ErrorResponse відповідь API
     */
    @ExceptionHandler(value = ProgPlatformNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse errorProgNotFound(ProgPlatformNotFoundException ex) {
        log.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), ex.getMessage(), ex);
        return new ErrorResponse(HttpStatus.NOT_FOUND, new ErrorData(
                "Не знайдено об'єкт", ex.getMessage(), MDC.get("msid")
        ));
    }

    /**
     * Помилка відмови в доступі
     *
     * @param e помилка
     * @return ErrorResponse відповідь API
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse accessDenied(AccessDeniedException e) {
        log.error("MSID: {}, Повідомлення помилки: {}", MDC.get("msid"), e.getMessage(), e);
        return new ErrorResponse(HttpStatus.FORBIDDEN, new ErrorData(
                "Відмовлено в доступі", "У вас не має доступу до ресурсу", MDC.get("msid")
        ));
    }

    /**
     * Помилка валідації вхідних параметрів
     *
     * @param ex помилка
     * @return FieldValidResponse відповідь API
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FieldValidResponse handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new FieldValidResponse(new FieldValidData(
                "Помилка валідції вхідних параметрів", errors
        ));
    }
}
