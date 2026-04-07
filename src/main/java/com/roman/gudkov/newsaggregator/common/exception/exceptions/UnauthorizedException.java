package com.roman.gudkov.newsaggregator.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Исключение для ошибок authentication/authorization.
 *
 * <p>Выбрасывается при неверных credentials
 * или отсутствии доступа.</p>
 */
public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
