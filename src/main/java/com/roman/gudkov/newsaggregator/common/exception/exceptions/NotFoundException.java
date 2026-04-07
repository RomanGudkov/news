package com.roman.gudkov.newsaggregator.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Исключение для ситуации, когда ресурс не найден (HTTP 404).
 */
public class NotFoundException extends ApiException {

    /**
     * @param message сообщение об ошибке
     */
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
