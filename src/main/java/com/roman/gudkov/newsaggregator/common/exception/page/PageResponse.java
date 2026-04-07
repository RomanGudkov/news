package com.roman.gudkov.newsaggregator.common.exception.page;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Обёртка для ответа с пагинированными данными.
 *
 * <p>Используется для стандартизации ответов API при работе с большими списками сущностей.</p>
 *
 * @param <T> тип элементов в ответе
 */
@Data
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
