package com.roman.gudkov.newsaggregator.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Article}.
 *
 * <p>Поддерживает CRUD операции и динамические запросы через Specification.</p>
 */
public interface ArticleRepository extends JpaRepository<Article, Long>,
        JpaSpecificationExecutor<Article> {

    /**
     * Возвращает список статей по идентификатору категории.
     *
     * @param categoryId идентификатор категории
     * @return список статей
     */
    List<Article> findByCategoryId(Long categoryId);

    /**
     * Проверяет существование заголовка статьи в указанной категории.
     *
     * @param title      заголовок статьи
     * @param categoryId id категории
     * @return {@code true}, если категория существует, иначе {@code false}
     */
    boolean existsByTitleAndCategoryId(String title, Long categoryId);
}
