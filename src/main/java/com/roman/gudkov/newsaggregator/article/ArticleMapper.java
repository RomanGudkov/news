package com.roman.gudkov.newsaggregator.article;

import com.roman.gudkov.newsaggregator.article.dto.ArticleResponse;
import com.roman.gudkov.newsaggregator.article.dto.CreateArticleRequest;
import com.roman.gudkov.newsaggregator.category.Category;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Маппер для преобразования объектов статьи.
 *
 * <p>Отвечает за конвертацию между сущностью {@link Article}
 * и DTO слоя API.</p>
 */
@Mapper(componentModel = SPRING)
public interface ArticleMapper {

    /**
     * Преобразует DTO создания статьи в сущность.
     *
     * @param request данные для создания статьи
     * @return новая сущность статьи
     */
    default Article toEntity(CreateArticleRequest request) {
        return new Article(request.getTitle(), request.getText());
    }

    /**
     * Преобразует сущность статьи в DTO ответа.
     *
     * @param article сущность статьи
     * @return DTO с данными статьи
     */
    default ArticleResponse toResponse(Article article) {
        Category category = article.getCategory();

        String categoryName = category != null ? category.getName() : null;

        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getText(),
                article.getCreatedAt(),
                categoryName
        );
    }
}
