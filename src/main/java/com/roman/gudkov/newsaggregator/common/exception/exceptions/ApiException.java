package com.roman.gudkov.newsaggregator.common.exception.exceptions;

import lombok.Getter;

/**
 * Базовое исключение для ошибок API.
 *
 * <p>Содержит HTTP статус и сообщение, которые будут возвращены клиенту.</p>
 *
 * <p>Используется как родительский класс для всех пользовательских
 * исключений (например: NotFound, Conflict, BadRequest).</p>
 */
@Getter
public class ApiException extends RuntimeException {

    private final int status;

    /**
     * @param message сообщение об ошибке
     * @param status  HTTP статус
     */
    protected ApiException(String message, int status) {
        super(message);
        this.status = status;
    }
}
