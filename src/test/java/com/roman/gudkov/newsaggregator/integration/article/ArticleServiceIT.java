package com.roman.gudkov.newsaggregator.integration.article;

import com.roman.gudkov.newsaggregator.article.Article;
import com.roman.gudkov.newsaggregator.article.ArticleRepository;
import com.roman.gudkov.newsaggregator.category.Category;
import com.roman.gudkov.newsaggregator.category.CategoryRepository;
import com.roman.gudkov.newsaggregator.integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ArticleServiceIT extends BaseIntegrationTest {

    private static final String TITLE = "Test title";
    private static final String UPDATED_TITLE = "new Test title";
    private static final String TEXT = "Test text";
    private static final String CATEGORY_NAME = "Tech";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should save article")
    void shouldSaveArticle() {
        Category category = categoryRepository.save(defaultCategory());

        Article article = defaultArticle();
        article.setCategory(category);

        Article saved = articleRepository.save(article);

        Article found = articleRepository.findById(saved.getId()).orElseThrow();

        assertNotNull(found);
        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("Should find saved article")
    void shouldFindSavedArticle() {
        Category category = categoryRepository.save(defaultCategory());

        Article article = defaultArticle();
        article.setCategory(category);

        Article saved = articleRepository.save(article);

        Article found = articleRepository.findById(saved.getId()).orElseThrow();

        assertNotNull(found);
        assertEquals(TITLE, found.getTitle());
        assertEquals(TEXT, found.getText());
    }

    @Test
    @DisplayName("Should load category lazily inside transaction")
    @Transactional
    void shouldLoadLazyRelation() {
        Category category = categoryRepository.save(defaultCategory());

        Article article = defaultArticle();
        article.setCategory(category);

        Article saved = articleRepository.save(article);

        Article found = articleRepository.findById(saved.getId()).orElseThrow();

        String categoryName = found.getCategory().getName();

        assertEquals(CATEGORY_NAME, categoryName);
    }

    @Test
    @Transactional
    @DisplayName("Should update article via dirty checking")
    void shouldUpdateArticleViaDirtyChecking() {
        Category category = categoryRepository.save(defaultCategory());

        Article article = defaultArticle();
        article.setCategory(category);

        Article saved = articleRepository.save(article);

        saved.changeTitle(UPDATED_TITLE);
        articleRepository.flush();

        Article found = articleRepository.findById(saved.getId()).orElseThrow();

        assertEquals(UPDATED_TITLE, found.getTitle());
    }

    @Test
    @DisplayName("Should not update detached entity")
    void shouldNotUpdateDetachedEntity() {
        Category category = categoryRepository.save(defaultCategory());

        Article article = defaultArticle();
        article.setCategory(category);

        Article saved = articleRepository.save(article);

        saved.changeTitle(UPDATED_TITLE);

        Article found = articleRepository.findById(saved.getId()).orElseThrow();

        assertEquals(TITLE, found.getTitle());
    }

    @Test
    @DisplayName("Should update detached entity via save")
    void shouldUpdateDetachedEntityViaSave() {
        Category category = categoryRepository.save(defaultCategory());

        Article article = defaultArticle();
        article.setCategory(category);

        Article saved = articleRepository.save(article);

        saved.changeTitle(UPDATED_TITLE);

        articleRepository.save(saved);

        Article found = articleRepository.findById(saved.getId()).orElseThrow();

        assertEquals(UPDATED_TITLE, found.getTitle());
    }

    @Test
    @DisplayName("Should delete article")
    void shouldDeleteArticle() {

        Category category = categoryRepository.save(defaultCategory());

        Article article = new Article(TITLE, TEXT);
        article.setCategory(category);

        Article saved = articleRepository.save(article);

        articleRepository.deleteById(saved.getId());

        assertFalse(articleRepository.findById(saved.getId()).isPresent());
    }

    private Category defaultCategory() {
        return new Category(CATEGORY_NAME);
    }

    private Article defaultArticle() {
        return new Article(TITLE, TEXT);
    }
}
