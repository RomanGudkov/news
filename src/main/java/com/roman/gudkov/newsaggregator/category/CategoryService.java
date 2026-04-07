package com.roman.gudkov.newsaggregator.category;

import com.roman.gudkov.newsaggregator.category.dto.CategoryResponse;
import com.roman.gudkov.newsaggregator.category.dto.CreateCategoryRequest;
import com.roman.gudkov.newsaggregator.category.dto.UpdateCategoryRequest;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.ConflictException;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.NotFoundException;
import com.roman.gudkov.newsaggregator.common.exception.page.PageMapper;
import com.roman.gudkov.newsaggregator.common.exception.page.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для работы с категориями.
 *
 * <p>Содержит бизнес-логику управления категориями:
 * получение, создание, обновление и удаление.</p>
 */
@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    /**
     * Возвращает категорию по идентификатору.
     *
     * @param id идентификатор категории
     * @return категория
     * @throws NotFoundException если категория не найдена
     */
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        log.info("Get category by id: {}", id);
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found."));

        return mapper.toResponse(category);
    }

    /**
     * Получает список категорий с пагинацией.
     *
     * @param pageable параметры пагинации
     * @return страница категорий
     */
    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> findAll(Pageable pageable) {
        log.info("Get categories");
        Page<Category> page = repository.findAll(pageable);

        return PageMapper.map(page, mapper::toResponse);
    }

    /**
     * Создаёт новую категорию.
     *
     * @param body данные для создания
     * @return созданная категория
     * @throws ConflictException если категория уже существует
     */
    @Transactional
    public CategoryResponse create(CreateCategoryRequest body) {
        log.info("Create category with name: {}", body.getName());

        Category category = mapper.toEntity(body);

        try {
            repository.save(category);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    "Category with name '%s' already exists.".formatted(body.getName())
            );
        }

        return mapper.toResponse(category);
    }

    /**
     * Обновляет существующую категорию.
     *
     * @param id   идентификатор категории
     * @param body новые данные
     * @return обновлённая категория
     * @throws NotFoundException если категория не найдена
     */
    @Transactional
    public CategoryResponse update(Long id, UpdateCategoryRequest body) {
        log.info("Update category with id: {}", id);
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found."));

        category.changeName(body.getName());

        return mapper.toResponse(category);
    }

    /**
     * Удаляет категорию по идентификатору.
     *
     * @param id идентификатор категории
     * @throws NotFoundException если категория не найдена
     */
    @Transactional
    public void deleteById(Long id) {
        log.info("Delete category by id: {}", id);
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found."));

        repository.deleteById(id);
    }
}
