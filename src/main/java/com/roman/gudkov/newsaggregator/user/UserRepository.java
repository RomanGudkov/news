package com.roman.gudkov.newsaggregator.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями.
 */
public interface UserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Находит пользователя по username.
     *
     * @param username имя пользователя
     * @return Optional с пользователем
     */
    Optional<AppUser> findByUsername(String username);
}
