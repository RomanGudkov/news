package com.roman.gudkov.newsaggregator.article.specification;

import com.roman.gudkov.newsaggregator.article.Article;
import com.roman.gudkov.newsaggregator.article.ArticleFilter;
import org.springframework.data.jpa.domain.Specification;

/**
 * Спецификация для динамической фильтрации сущности {@link Article}.
 *
 * <p>Позволяет фильтровать статьи по:
 * <ul>
 *     <li>категории</li>
 *     <li>ключевому слову (title + content)</li>
 * </ul>
 * </p>
 */
public class ArticleSpecification {

    /**
     * Создаёт спецификацию на основе переданных фильтров.
     *
     * @param filter параметры фильтрации
     * @return спецификация для запроса Article
     */
    public static Specification<Article> withFilter(ArticleFilter filter) {
        if (filter == null) {
            return Specification.where(null);
        }

        return Specification
                .where(hasCategory(filter.getCategoryId()))
                .and(hasKeyword(filter.getKeyword()));
    }

    /**
     * Фильтр по ID категории.
     *
     * @param categoryId идентификатор категории
     * @return спецификация фильтрации по категории
     */
    private static Specification<Article> hasCategory(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null
                        ? null
                        : cb.equal(root.get("category").get("id"), categoryId);
    }

    /**
     * Поиск по ключевому слову в заголовке и содержимом статьи.
     *
     * @param keyword строка поиска
     * @return спецификация текстового поиска
     */
    private static Specification<Article> hasKeyword(String keyword) {
        return (root, query, cb) -> {

            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("text")), pattern)
            );
        };
    }
}
