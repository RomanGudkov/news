package com.roman.gudkov.newsaggregator.common.exception.handler;

import com.roman.gudkov.newsaggregator.common.exception.exceptions.ApiException;
import com.roman.gudkov.newsaggregator.common.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений REST API.
 *
 * <p>Перехватывает исключения, возникающие в контроллерах,
 * и преобразует их в единый формат {@link ErrorResponse}.</p>
 *
 * <p>Обрабатывает как пользовательские исключения ({@link ApiException}),
 * так и стандартные исключения Spring и Java.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает пользовательские исключения API.
     *
     * @param ex      исключение
     * @param request HTTP-запрос
     * @return HTTP-ответ с соответствующим статусом и сообщением
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), ex.getStatus(), request);
    }

    /**
     * Обрабатывает ошибки валидации {@code @Valid}.
     *
     * @param ex      исключение валидации
     * @param request HTTP-запрос
     * @return HTTP-ответ с кодом 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation Error");
        return buildResponse(message, HttpStatus.BAD_REQUEST.value(), request);
    }

    /**
     * Обрабатывает ошибки некорректных аргументов.
     *
     * @param ex      исключение
     * @param request HTTP-запрос
     * @return HTTP-ответ с кодом 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), request);
    }

    /**
     * Обрабатывает все необработанные исключения.
     *
     * <p>Логирует ошибку и возвращает обобщённое сообщение клиенту.</p>
     *
     * @param ex      исключение
     * @param request HTTP-запрос
     * @return HTTP-ответ с кодом 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception", ex);
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), request);
    }

    /**
     * Обрабатывает ошибки авторизации.
     *
     * @param ex      исключение авторизации
     * @param request HTTP-запрос
     * @return HTTP-ответ с кодом 401
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildResponse(
                "Invalid username or password",
                HttpStatus.UNAUTHORIZED.value(),
                request
        );
    }

    /**
     * Формирует объект ответа с ошибкой.
     */
    private ResponseEntity<ErrorResponse> buildResponse(String message, int status, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(message, status, request.getRequestURI(), LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }
}
