package com.roman.gudkov.newsaggregator.category;

import com.roman.gudkov.newsaggregator.category.dto.CategoryResponse;
import com.roman.gudkov.newsaggregator.category.dto.CreateCategoryRequest;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * Маппер для преобразования объектов категории.
 *
 * <p>Отвечает за конвертацию между DTO и сущностью {@link Category}.</p>
 */
@Mapper(componentModel = SPRING)
public interface CategoryMapper {

    /**
     * Преобразует DTO создания категории в сущность.
     *
     * @param request данные для создания категории
     * @return новая сущность категории
     */
    default Category toEntity(CreateCategoryRequest request) {
        return new Category(request.getName());
    }

    /**
     * Преобразует сущность категории в DTO ответа.
     *
     * @param category сущность категории
     * @return DTO с данными категории
     */
    CategoryResponse toResponse(Category category);
}
