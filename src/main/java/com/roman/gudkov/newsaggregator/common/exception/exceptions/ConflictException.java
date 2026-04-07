package com.roman.gudkov.newsaggregator.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Исключение для конфликтов данных (HTTP 409).
 *
 * <p>Например, попытка создать уже существующий ресурс.</p>
 */
public class ConflictException extends ApiException {

    /**
     * @param message описание конфликта
     */
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }
}
