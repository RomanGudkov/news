package com.roman.gudkov.newsaggregator.common.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Константы для валидации.
 *
 * <p>Содержит сообщения об ошибках и ограничения для полей.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationConstants {

    //  Category

    public static final int CATEGORY_NAME_MAX_LENGTH = 255;

    public static final String CATEGORY_NAME_NOT_BLANK = "Category name cannot be blank";
    public static final String CATEGORY_NAME_TOO_LONG = "Category name must be less than 255 characters";

    public static final String CATEGORY_ID_NOT_NULL = "Category id cannot be null";
    public static final String CATEGORY_ID_BE_POSITIVE = "Category id must be positive";


    //  Article

    public static final int ARTICLE_TITLE_MAX_LENGTH = 255;

    public static final String ARTICLE_TITLE_NOT_BLANK = "Article title cannot be blank";
    public static final String ARTICLE_TEXT_NOT_BLANK = "Article text cannot be blank";
    public static final String ARTICLE_TITLE_TOO_LONG = "Article title must be less than 255 characters";
}
