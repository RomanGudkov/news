package com.roman.gudkov.newsaggregator.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO ответа с информацией о категории.
 *
 * <p>Используется для передачи данных категории клиенту.</p>
 */
@Getter
@AllArgsConstructor
public class CategoryResponse {

    private final Long id;
    private final String name;
}
