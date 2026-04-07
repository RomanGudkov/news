package com.roman.gudkov.newsaggregator.category;

import com.roman.gudkov.newsaggregator.category.dto.CategoryResponse;
import com.roman.gudkov.newsaggregator.category.dto.CreateCategoryRequest;
import com.roman.gudkov.newsaggregator.category.dto.UpdateCategoryRequest;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.NotFoundException;
import com.roman.gudkov.newsaggregator.common.exception.model.ErrorResponse;
import com.roman.gudkov.newsaggregator.common.exception.page.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для работы с категориями.
 * Предоставляет API для получения, создания, обновления и удаления категорий.
 */
@Tag(name = "Categories", description = "Category management API")
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService service;

    /**
     * @param service сервис категорий
     */
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    /**
     * Возвращает категорию по её идентификатору.
     *
     * @param id идентификатор категории
     * @return категория в формате {@link CategoryResponse}
     * @throws NotFoundException если категория не найдена
     */
    @Operation(summary = "Get category by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Получает список категорий с пагинацией.
     *
     * @param pageable параметры пагинации и сортировки
     * @return страница категорий
     */
    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "Category found")
    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> getCategories(
            @PageableDefault(sort = "name") Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    /**
     * Создаёт новую категорию.
     *
     * @param body данные для создания категории (валидируются)
     * @return созданная категория
     */
    @Operation(summary = "Create category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Category already exists",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest body) {
        CategoryResponse response = service.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Обновляет существующую категорию.
     *
     * @param id   идентификатор категории
     * @param body данные для обновления (валидируются)
     * @return обновлённая категория
     * @throws NotFoundException если категория не найдена
     */
    @Operation(summary = "Update category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Category already exists",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable("id") Long id, @Valid @RequestBody UpdateCategoryRequest body) {
        return ResponseEntity.ok(service.update(id, body));
    }

    /**
     * Удаляет категорию по идентификатору.
     *
     * @param id идентификатор категории
     * @return пустой ответ с кодом 200 при успешном удалении
     * @throws NotFoundException если категория не найдена
     */
    @Operation(summary = "Delete category")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
