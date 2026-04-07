package com.roman.gudkov.newsaggregator.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TEXT_NOT_BLANK;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TITLE_MAX_LENGTH;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TITLE_NOT_BLANK;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TITLE_TOO_LONG;

/**
 * DTO запроса на обновление статьи.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleRequest {

    @NotBlank(message = ARTICLE_TITLE_NOT_BLANK)
    @Size(max = ARTICLE_TITLE_MAX_LENGTH, message = ARTICLE_TITLE_TOO_LONG)
    private String title;

    @NotBlank(message = ARTICLE_TEXT_NOT_BLANK)
    private String text;
}
