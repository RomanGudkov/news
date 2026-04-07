package com.roman.gudkov.newsaggregator.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * DTO ответа со статьёй.
 *
 * <p>Используется для передачи данных статьи клиенту.</p>
 */
@Getter
@AllArgsConstructor
public class ArticleResponse {
    private final Long id;
    private final String title;
    private final String text;
    private final Instant date;
    private final String category;
}