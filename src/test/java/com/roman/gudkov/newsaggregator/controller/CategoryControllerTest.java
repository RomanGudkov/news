package com.roman.gudkov.newsaggregator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roman.gudkov.newsaggregator.category.CategoryController;
import com.roman.gudkov.newsaggregator.category.CategoryService;
import com.roman.gudkov.newsaggregator.category.dto.CategoryResponse;
import com.roman.gudkov.newsaggregator.category.dto.CreateCategoryRequest;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.ConflictException;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.NotFoundException;
import com.roman.gudkov.newsaggregator.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    private static final Long ID = 1L;
    private static final String CATEGORY_NAME = "Tech";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService service;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Should return category by id")
    void getCategory_shouldReturnCategory() throws Exception {
        CategoryResponse response = defaultResponse();

        when(service.findById(ID)).thenReturn(response);

        mockMvc.perform(get("/api/v1/categories/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME));
    }

    @Test
    @DisplayName("Should return 404 when category not found")
    void getCategory_shouldReturn404() throws Exception {

        when(service.findById(ID)).thenThrow(new NotFoundException("Category not found"));

        mockMvc.perform(get("/api/v1/categories/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create category")
    void create_shouldCreateCategory() throws Exception {
        CreateCategoryRequest request = defaultRequest();
        CategoryResponse response = defaultResponse();

        when(service.create(any(CreateCategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME));
    }

    @Test
    @DisplayName("Should return 400 when category name is blank")
    void create_shouldReturnBadRequest_whenNameIsNull() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("");

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 when category already exists")
    void create_shouldReturnConflict_whenCategoryExists() throws Exception {
        CreateCategoryRequest request = defaultRequest();

        when(service.create(any(CreateCategoryRequest.class)))
                .thenThrow(new ConflictException("Category already exists"));

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    private CreateCategoryRequest defaultRequest() {
        return new CreateCategoryRequest(CATEGORY_NAME);
    }

    private CategoryResponse defaultResponse() {
        return new CategoryResponse(ID, CATEGORY_NAME);
    }
}
