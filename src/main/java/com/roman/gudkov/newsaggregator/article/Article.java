package com.roman.gudkov.newsaggregator.article;

import com.roman.gudkov.newsaggregator.category.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TEXT_NOT_BLANK;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TITLE_MAX_LENGTH;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TITLE_NOT_BLANK;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.ARTICLE_TITLE_TOO_LONG;

/**
 * Сущность статьи.
 *
 * <p>Представляет новостную статью в системе и хранит
 * основные данные, а также связь с категорией.</p>
 */
@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(
        name = "articles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_article_title_category",
                        columnNames = {"title", "category_id"}
                )
        },
        indexes = {
                @Index(name = "idx_articles_category", columnList = "category_id"),
                @Index(name = "idx_articles_created_at", columnList = "created_at")
        }
)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = ARTICLE_TITLE_MAX_LENGTH)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * @param title заголовок статьи
     * @param text  текст статьи
     */
    public Article(String title, String text) {
        this.title = validateTitle(title);
        this.text = validateText(text);
    }

    /**
     * Изменяет заголовок статьи.
     *
     * @param title новый заголовок
     */
    public void changeTitle(String title) {
        this.title = validateTitle(title);
    }

    /**
     * Изменяет текст статьи.
     *
     * @param text новый текст
     */
    public void changeText(String text) {
        this.text = validateText(text);
    }

    /**
     * Устанавливает категорию статьи.
     *
     * @param category категория
     * @throws IllegalArgumentException если категория равна null
     */
    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
    }

    /**
     * Устанавливает дату создания перед сохранением в БД.
     */
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        }
    }

    /**
     * Валидирует заголовок статьи.
     */
    private static String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(ARTICLE_TITLE_NOT_BLANK);
        }
        title = title.trim();

        if (title.length() > ARTICLE_TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException(ARTICLE_TITLE_TOO_LONG);
        }
        return title;
    }

    /**
     * Валидирует текст статьи.
     */
    private static String validateText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(ARTICLE_TEXT_NOT_BLANK);
        }
        return text.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return id != null && Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
