package com.roman.gudkov.newsaggregator.article;

import com.roman.gudkov.newsaggregator.article.dto.ArticleResponse;
import com.roman.gudkov.newsaggregator.article.dto.CreateArticleRequest;
import com.roman.gudkov.newsaggregator.article.dto.UpdateArticleRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер для работы со статьями.
 *
 * <p>Предоставляет API для получения, создания и обновления статей,
 * а также получения статей по категории.</p>
 */
@Tag(name = "Articles", description = "Article management API")
@RestController
@RequestMapping("api/v1/articles")
public class ArticleController {

    private final ArticleService service;

    /**
     * @param service сервис для работы со статьями
     */
    public ArticleController(ArticleService service) {
        this.service = service;
    }

    /**
     * Возвращает статью по идентификатору.
     *
     * @param id идентификатор статьи
     * @return статья
     */
    @Operation(summary = "Get article by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Article found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Article not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Получает список статей с поддержкой фильтрации и пагинации.
     *
     * @param filter   фильтры (categoryId, keyword)
     * @param pageable параметры пагинации и сортировки
     * @return страница статей
     */
    @Operation(summary = "Get all articles")
    @ApiResponse(responseCode = "200", description = "Articles found")
    @GetMapping
    public ResponseEntity<PageResponse<ArticleResponse>> getArticles(
            ArticleFilter filter,
            @PageableDefault(sort = "title") Pageable pageable) {
        return ResponseEntity.ok(service.findArticles(filter, pageable));
    }

    /**
     * Возвращает статьи по идентификатору категории.
     *
     * @param id идентификатор категории
     * @return список статей категории
     */
    @Operation(summary = "Get articles by category id")
    @ApiResponse(responseCode = "200", description = "Articles found")
    @GetMapping("/category/{id}")
    public ResponseEntity<List<ArticleResponse>> getArticlesByCategory(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findByCategory(id));
    }

    /**
     * Создаёт новую статью.
     *
     * @param body данные для создания статьи
     * @return созданная статья
     */
    @Operation(summary = "Create article")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Article created"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Category with id not found.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Article already exists",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ArticleResponse> create(@Valid @RequestBody CreateArticleRequest body) {
        ArticleResponse response = service.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Обновляет существующую статью.
     *
     * @param id   идентификатор статьи
     * @param body новые данные статьи
     * @return обновлённая статья
     */
    @Operation(summary = "Update article")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Article updated"),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(
            @PathVariable("id") Long id, @Valid @RequestBody UpdateArticleRequest body) {
        return ResponseEntity.ok(service.update(id, body));
    }
}