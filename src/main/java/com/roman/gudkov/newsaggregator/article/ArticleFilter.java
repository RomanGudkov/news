package com.roman.gudkov.newsaggregator.article;

import lombok.Getter;
import lombok.Setter;

/**
 * Фильтры для поиска статей.
 *
 * <p>Используется в API для динамической фильтрации списка статей.</p>
 */
@Getter
@Setter
public class ArticleFilter {
    private Long categoryId;
    private String keyword;
}
