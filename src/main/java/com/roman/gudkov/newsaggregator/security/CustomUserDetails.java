package com.roman.gudkov.newsaggregator.security;

import com.roman.gudkov.newsaggregator.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Security-модель пользователя для Spring Security.
 *
 * <p>Адаптирует {@link AppUser} под интерфейс
 * {@link UserDetails}.</p>
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final AppUser user;

    /**
     * Возвращает authorities пользователя.
     *
     * @return список ролей пользователя
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()
                )
        );
    }

    /**
     * Возвращает hash пароля пользователя.
     *
     * @return encoded password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Возвращает username пользователя.
     *
     * @return username
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
