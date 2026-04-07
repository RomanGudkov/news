package com.roman.gudkov.newsaggregator.common.exception.page;

import org.springframework.data.domain.Page;

import java.util.function.Function;

/**
 * Маппер для преобразования Page сущностей в PageResponse DTO.
 */
public class PageMapper {

    /**
     * Преобразует Page сущностей в PageResponse DTO.
     *
     * @param page   страница сущностей
     * @param mapper функция преобразования сущности в DTO
     * @return PageResponse с DTO
     */
    public static <T, R> PageResponse<R> map(Page<T> page, Function<T, R> mapper) {
        return new PageResponse<>(
                page.getContent().stream()
                        .map(mapper)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
