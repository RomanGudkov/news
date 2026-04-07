package com.roman.gudkov.newsaggregator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roman.gudkov.newsaggregator.article.ArticleController;
import com.roman.gudkov.newsaggregator.article.ArticleService;
import com.roman.gudkov.newsaggregator.article.dto.ArticleResponse;
import com.roman.gudkov.newsaggregator.article.dto.CreateArticleRequest;
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

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ArticleControllerTest {

    private static final Long ID = 1L;
    private static final String TITLE = "Test title";
    private static final String TEXT = "Test text";
    private static final String CATEGORY_NAME = "Tech";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService service;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Should return article by id")
    void getArticle_shouldReturnArticle() throws Exception {
        ArticleResponse response = defaultResponse();

        when(service.findById(ID)).thenReturn(response);

        mockMvc.perform(get("/api/v1/articles/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.text").value(TEXT))
                .andExpect(jsonPath("$.category").value(CATEGORY_NAME));
    }

    @Test
    @DisplayName("Should return 404 when article not found")
    void getArticle_shouldReturn404() throws Exception {

        when(service.findById(ID))
                .thenThrow(new NotFoundException("Article not found."));

        mockMvc.perform(get("/api/v1/articles/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create article")
    void create_shouldCreateArticle() throws Exception {
        CreateArticleRequest request = defaultRequest();
        ArticleResponse response = defaultResponse();

        when(service.create(any(CreateArticleRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.text").value(TEXT))
                .andExpect(jsonPath("$.category").value(CATEGORY_NAME));
    }

    @Test
    @DisplayName("Should return 400 when article title is blank")
    void create_shouldReturnBadRequest_whenTitleIsBlank() throws Exception {
        CreateArticleRequest request = new CreateArticleRequest("", TEXT, ID);

        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 when article exists")
    void create_shouldReturnConflict_whenArticleExists() throws Exception {
        CreateArticleRequest request = defaultRequest();

        when(service.create(any(CreateArticleRequest.class)))
                .thenThrow(new ConflictException("Article already exists"));

        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return 404 when category not found")
    void create_shouldReturnNotFound_whenCategoryNotFound() throws Exception {
        CreateArticleRequest request = defaultRequest();

        when(service.create(any(CreateArticleRequest.class)))
                .thenThrow(new NotFoundException("Category not found."));

        mockMvc.perform(post("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    private CreateArticleRequest defaultRequest() {
        return new CreateArticleRequest(TITLE, TEXT, ID);
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
