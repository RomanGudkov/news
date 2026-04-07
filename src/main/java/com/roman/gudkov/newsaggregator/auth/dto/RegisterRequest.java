package com.roman.gudkov.newsaggregator.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO запроса регистрации пользователя.
 */
public record RegisterRequest(

        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
