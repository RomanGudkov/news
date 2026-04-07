package com.roman.gudkov.newsaggregator.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TEXT_NOT_BLANK;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TITLE_MAX_LENGTH;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TITLE_NOT_BLANK;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TITLE_TOO_LONG;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.CATEGORY_ID_BE_POSITIVE;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.CATEGORY_ID_NOT_NULL;

/**
 * DTO запроса на создание статьи.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateArticleRequest {

    @NotBlank(message = ARTICLE_TITLE_NOT_BLANK)
    @Size(max = ARTICLE_TITLE_MAX_LENGTH, message = ARTICLE_TITLE_TOO_LONG)
    private String title;

    @NotBlank(message = ARTICLE_TEXT_NOT_BLANK)
    private String text;

    @NotNull(message = CATEGORY_ID_NOT_NULL)
    @Positive(message = CATEGORY_ID_BE_POSITIVE)
    private Long categoryId;
}

