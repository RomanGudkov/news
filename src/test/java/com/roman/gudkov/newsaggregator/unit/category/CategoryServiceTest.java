package com.roman.gudkov.newsaggregator.unit.category;

import com.roman.gudkov.newsaggregator.category.Category;
import com.roman.gudkov.newsaggregator.category.CategoryMapper;
import com.roman.gudkov.newsaggregator.category.CategoryRepository;
import com.roman.gudkov.newsaggregator.category.CategoryService;
import com.roman.gudkov.newsaggregator.category.dto.CategoryResponse;
import com.roman.gudkov.newsaggregator.category.dto.CreateCategoryRequest;
import com.roman.gudkov.newsaggregator.category.dto.UpdateCategoryRequest;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.ConflictException;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private static final Long ID = 1L;
    private static final String CATEGORY_NAME = "Tech";
    private static final String NEW_CATEGORY_NAME = "new Tech";

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CategoryService service;

    @Test
    @DisplayName("Should return category")
    void findById_shouldFindCategory() {
        Category category = defaultCategory();
        CategoryResponse response = defaultResponse();

        when(repository.findById(ID)).thenReturn(Optional.of(category));
        when(mapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = service.findById(ID);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());

        verify(repository).findById(ID);
        verify(mapper).toResponse(category);
    }

    @Test
    @DisplayName("Should throw NotFoundException when category not found")
    void findById_shouldThrowNotFound_whenCategoryNotFound() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(ID));

        verify(repository).findById(ID);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should create category")
    void create_shouldCreateCategory() {
        CreateCategoryRequest request = new CreateCategoryRequest(CATEGORY_NAME);
        Category category = defaultCategory();
        CategoryResponse response = defaultResponse();


        when(mapper.toEntity(request)).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = service.create(request);

        assertEquals(response.getName(), result.getName());

        verify(mapper).toEntity(request);
        verify(repository).save(category);
        verify(mapper).toResponse(category);
    }

    @Test
    @DisplayName("Should throw ConflictException when category already exists")
    void create_shouldThrowConflict_whenCategoryExists() {
        CreateCategoryRequest request = new CreateCategoryRequest(CATEGORY_NAME);
        Category category = defaultCategory();

        when(mapper.toEntity(request)).thenReturn(category);

        when(repository.save(category)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(ConflictException.class, () -> service.create(request));

        verify(mapper).toEntity(request);
        verify(repository).save(category);
        verify(mapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should update category name")
    void update_shouldUpdateCategory() {
        Category category = defaultCategory();
        CategoryResponse response = updatedResponse();
        UpdateCategoryRequest newCategory = new UpdateCategoryRequest(NEW_CATEGORY_NAME);

        when(repository.findById(ID)).thenReturn(Optional.of(category));
        when(mapper.toResponse(category)).thenReturn(response);

        CategoryResponse result = service.update(ID, newCategory);

        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());

        verify(repository).findById(ID);
        verify(mapper).toResponse(category);
    }

    @Test
    @DisplayName("Should delete category")
    void delete_shouldDeleteCategory() {
        Category category = defaultCategory();

        when(repository.findById(ID)).thenReturn(Optional.of(category));

        service.deleteById(ID);

        verify(repository).findById(ID);
        verify(repository).deleteById(ID);
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting missing category")
    void delete_shouldThrowNotFound_whenCategoryNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteById(ID));

        verify(repository).findById(ID);
        verify(repository, never()).deleteById(any());
    }

    private Category defaultCategory() {
        return new Category(CATEGORY_NAME);
    }

    private CategoryResponse defaultResponse() {
        return new CategoryResponse(
                ID,
                CATEGORY_NAME
        );
    }

    private CategoryResponse updatedResponse() {
        return new CategoryResponse(
                ID,
                NEW_CATEGORY_NAME
        );
    }
}
