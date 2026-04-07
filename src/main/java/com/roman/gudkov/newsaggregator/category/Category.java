package com.roman.gudkov.newsaggregator.category;

import com.roman.gudkov.newsaggregator.article.Article;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.CATEGORY_NAME_MAX_LENGTH;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.CATEGORY_NAME_NOT_BLANK;
import static com.roman.gudkov.newsaggregator.common.validation.ValidationConstants.CATEGORY_NAME_TOO_LONG;

/**
 * Сущность категории.
 *
 * <p>Представляет категорию новостей и содержит список связанных статей.</p>
 *
 * <p>Гарантирует корректность состояния через валидацию названия
 * при создании и изменении.</p>
 */
@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_category_name", columnNames = "name")
        }
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = CATEGORY_NAME_MAX_LENGTH)
    private String name;

    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private final List<Article> articles = new ArrayList<>();

    /**
     * @param name название категории
     * @throws IllegalArgumentException если название некорректно
     */
    public Category(String name) {
        this.name = validateName(name);
    }

    /**
     * Изменяет название категории.
     *
     * @param name новое название
     * @throws IllegalArgumentException если название некорректно
     */
    public void changeName(String name) {
        this.name = validateName(name);
    }

    /**
     * Добавляет статью в категорию.
     *
     * <p>Обеспечивает согласованность двусторонней связи:
     * устанавливает категорию у статьи.</p>
     *
     * @param article статья
     * @throws IllegalArgumentException если статья равна {@code null}
     */
    public void addArticle(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null");
        }
        if (!articles.contains(article)) {
            articles.add(article);
            article.setCategory(this);
        }
    }

    /**
     * Валидирует и нормализует название категории.
     *
     * @param name исходное название
     * @return валидное название
     * @throws IllegalArgumentException если название пустое или превышает допустимую длину
     */
    private static String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(CATEGORY_NAME_NOT_BLANK);
        }
        name = name.trim();

        if (name.length() > CATEGORY_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(CATEGORY_NAME_TOO_LONG);
        }
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
