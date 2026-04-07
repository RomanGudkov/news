package com.roman.gudkov.newsaggregator.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Репозиторий для работы с категориями.
 *
 * <p>Поддерживает стандартные CRUD операции и расширяемые запросы через Specification API.</p>
 */
public interface CategoryRepository extends JpaRepository<Category, Long>,
        JpaSpecificationExecutor<Category> {
}
