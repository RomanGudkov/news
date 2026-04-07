package com.roman.gudkov.newsaggregator.auth;

import com.roman.gudkov.newsaggregator.auth.dto.AuthResponse;
import com.roman.gudkov.newsaggregator.auth.dto.LoginRequest;
import com.roman.gudkov.newsaggregator.auth.dto.RegisterRequest;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.ConflictException;
import com.roman.gudkov.newsaggregator.common.exception.exceptions.UnauthorizedException;
import com.roman.gudkov.newsaggregator.security.JwtService;
import com.roman.gudkov.newsaggregator.user.AppUser;
import com.roman.gudkov.newsaggregator.user.Role;
import com.roman.gudkov.newsaggregator.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис аутентификации и регистрации пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String INVALID_CREDENTIALS = "Invalid credentials";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Регистрирует нового пользователя.
     *
     * @param request данные регистрации
     * @return ответ авторизации
     */
    public AuthResponse register(RegisterRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Username already exists");
        }

        return new AuthResponse("registered");
    }

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param request данные для входа
     * @return JWT токен
     */
    public AuthResponse login(LoginRequest request) {

        AppUser user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() -> new UnauthorizedException(INVALID_CREDENTIALS));

        boolean matches = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if (!matches) {
            throw new UnauthorizedException(INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
