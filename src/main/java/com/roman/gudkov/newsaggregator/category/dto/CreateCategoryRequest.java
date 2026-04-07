package com.roman.gudkov.newsaggregator.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.CATEGORY_NAME_MAX_LENGTH;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.CATEGORY_NAME_NOT_BLANK;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.CATEGORY_NAME_TOO_LONG;

/**
 * DTO запроса на создание категории.
 *
 * <p>Содержит данные, необходимые для создания новой категории.</p>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {

    @NotBlank(message = CATEGORY_NAME_NOT_BLANK)
    @Size(max = CATEGORY_NAME_MAX_LENGTH, message = CATEGORY_NAME_TOO_LONG)
    private String name;
}

