package com.roman.gudkov.newsaggregator.article;

import com.roman.gudkov.newsaggregator.article.dto.ArticleResponse;
import com.roman.gudkov.newsaggregator.article.dto.CreateArticleRequest;
import com.roman.gudkov.newsaggregator.article.dto.UpdateArticleRequest;
import com.roman.gudkov.newsaggregator.article.specification.ArticleSpecification;
import com.roman.gudkov.newsaggregator.category.Category;
import com.roman.gudkov.newsaggregator.category.CategoryRepository;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.ConflictException;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.NotFoundException;
import com.roman.gudkov.newsaggregator.common.exception.page.PageMapper;
import com.roman.gudkov.newsaggregator.common.exception.page.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы со статьями.
 *
 * <p>Содержит бизнес-логику управления статьями:
 * получение, создание, обновление и поиск по категории.</p>
 */
@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository repository;
    private final CategoryRepository categoryRepository;
    private final ArticleMapper mapper;

    /**
     * Возвращает статью по идентификатору.
     *
     * @param id идентификатор статьи
     * @return статья
     * @throws NotFoundException если статья не найдена
     */
    @Transactional(readOnly = true)
    public ArticleResponse findById(Long id) {
        log.info("Get article by id: {}", id);

        Article article = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Article with id " + id + " not found.")
                );

        return mapper.toResponse(article);
    }

    /**
     * Получает статьи с фильтрацией и пагинацией.
     *
     * @param filter   параметры фильтрации
     * @param pageable параметры пагинации
     * @return страница статей
     */
    @Transactional(readOnly = true)
    public PageResponse<ArticleResponse> findArticles(ArticleFilter filter, Pageable pageable) {
        log.info("Get articles with filter: {}", filter);
        Specification<Article> spec = ArticleSpecification.withFilter(filter);
        Page<Article> page = repository.findAll(spec, pageable);

        return PageMapper.map(page, mapper::toResponse);
    }

    /**
     * Возвращает список статей по категории.
     *
     * @param id идентификатор категории
     * @return список статей
     */
    @Transactional(readOnly = true)
    public List<ArticleResponse> findByCategory(Long id) {
        log.info("Get all articles by category: {}", id);

        List<Article> articles = repository.findByCategoryId(id);

        return articles.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Создаёт новую статью.
     *
     * @param request данные для создания статьи
     * @return созданная статья
     * @throws NotFoundException если категория не найдена
     */
    @Transactional
    public ArticleResponse create(CreateArticleRequest request) {
        log.info("Create article with title: {} in category: {}",
                request.getTitle(), request.getCategoryId());

        Long categoryId = request.getCategoryId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        "Category with id " + categoryId + " not found.")
                );

        if (repository.existsByTitleAndCategoryId(request.getTitle(), categoryId)) {
            throw new ConflictException("Article with title '%s' already exists in category %d"
                    .formatted(request.getTitle(), categoryId));
        }
        Article article = mapper.toEntity(request);

        category.addArticle(article);

        try {
            repository.save(article);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Duplicate article (title + category)");
        }

        return mapper.toResponse(article);
    }

    /**
     * Обновляет существующую статью.
     *
     * @param id      идентификатор статьи
     * @param request новые данные
     * @return обновлённая статья
     * @throws NotFoundException если статья не найдена
     */
    @Transactional
    public ArticleResponse update(Long id, UpdateArticleRequest request) {
        log.info("Update article by id: {}", id);

        Article article = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Article with id " + id + " not found."
                ));

        if (request.getTitle() != null) {
            article.changeTitle(request.getTitle());
        }
        if (request.getText() != null) {
            article.changeText(request.getText());
        }

        return mapper.toResponse(article);
    }
}
