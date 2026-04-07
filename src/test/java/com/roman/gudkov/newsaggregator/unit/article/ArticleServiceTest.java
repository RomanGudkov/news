package com.roman.gudkov.newsaggregator.unit.article;

import com.roman.gudkov.newsaggregator.article.Article;
import com.roman.gudkov.newsaggregator.article.ArticleMapper;
import com.roman.gudkov.newsaggregator.article.ArticleRepository;
import com.roman.gudkov.newsaggregator.article.ArticleService;
import com.roman.gudkov.newsaggregator.article.dto.ArticleResponse;
import com.roman.gudkov.newsaggregator.article.dto.CreateArticleRequest;
import com.roman.gudkov.newsaggregator.category.Category;
import com.roman.gudkov.newsaggregator.category.CategoryRepository;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.ConflictException;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    private static final Long ID = 1L;
    private static final String TITLE = "Test title";
    private static final String TEXT = "Test text";
    private static final String CATEGORY_NAME = "Tech";


    @Mock
    private ArticleRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ArticleMapper mapper;

    @InjectMocks
    private ArticleService service;

    @Test
    @DisplayName("Should return article")
    void findById_shouldFindArticle() {
        Article article = defaultArticle();
        ArticleResponse response = defaultResponse();

        when(repository.findById(ID)).thenReturn(Optional.of(article));

        when(mapper.toResponse(article)).thenReturn(response);

        ArticleResponse result = service.findById(ID);

        assertEquals(response.getTitle(), result.getTitle());
        assertEquals(response.getText(), result.getText());
        assertEquals(response.getCategory(), result.getCategory());

        verify(repository).findById(ID);
        verify(mapper).toResponse(article);
    }

    @Test
    @DisplayName("Should throw NotFoundException when article not found")
    void findById_shouldThrowNotFound_whenArticleNotFound() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(ID));

        verify(repository).findById(ID);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should create new article")
    void create_shouldCreateArticleSuccessfully() {
        CreateArticleRequest request = defaultRequest();
        Category category = defaultCategory();
        Article article = defaultArticle();
        ArticleResponse response = defaultResponse();

        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));

        when(repository.existsByTitleAndCategoryId(TITLE, ID)).thenReturn(false);

        when(mapper.toEntity(request)).thenReturn(article);

        when(repository.save(article)).thenReturn(article);

        when(mapper.toResponse(article)).thenReturn(response);

        ArticleResponse result = service.create(request);

        assertEquals(TITLE, result.getTitle());
        assertEquals(TEXT, result.getText());
        assertEquals(CATEGORY_NAME, result.getCategory());

        verify(categoryRepository).findById(ID);
        verify(repository).existsByTitleAndCategoryId(TITLE, ID);
        verify(mapper).toEntity(request);
        verify(repository).save(article);
        verify(mapper).toResponse(article);
    }

    @Test
    @DisplayName("Should throw NotFoundException when category not found")
    void create_shouldThrowNotFound_whenCategoryNotExist() {
        CreateArticleRequest request = defaultRequest();

        when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.create(request));

        verify(categoryRepository).findById(ID);
        verify(repository, never()).existsByTitleAndCategoryId(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ConflictException when article exist")
    void create_shouldThrowConflict_whenArticleExists() {
        CreateArticleRequest request = defaultRequest();
        Category category = defaultCategory();

        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));

        when(repository.existsByTitleAndCategoryId(TITLE, ID)).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.create(request));

        verify(categoryRepository).findById(ID);
        verify(repository).existsByTitleAndCategoryId(TITLE, ID);
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    private CreateArticleRequest defaultRequest() {
        return new CreateArticleRequest(TITLE, TEXT, ID);
    }

    private Category defaultCategory() {
        return new Category(CATEGORY_NAME);
    }

    private Article defaultArticle() {
        return new Article(TITLE, TEXT);
    }

    private ArticleResponse defaultResponse() {
        return new ArticleResponse(
                ID,
                TITLE,
                TEXT,
                null,
                CATEGORY_NAME
        );
    }
}
