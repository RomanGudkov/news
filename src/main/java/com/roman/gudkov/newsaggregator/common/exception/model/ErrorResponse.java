package com.roman.gudkov.newsaggregator.common.exception.model;

import java.time.LocalDateTime;

/**
 * DTO ответа с информацией об ошибке.
 *
 * @param message   сообщение об ошибке
 * @param status    HTTP статус
 * @param path      путь запроса
 * @param timestamp время возникновения ошибки
 */
public record ErrorResponse(String message, int status, String path, LocalDateTime timestamp) {
}
