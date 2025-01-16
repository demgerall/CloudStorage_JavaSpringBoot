package ru.netology.CloudStorage_SpringBoot.security.jwt_security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.CloudStorage_SpringBoot.entities.User;

import java.util.Collection;
import java.util.Collections;

//  JWT пользователь для Security компонентов

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JWTUser implements UserDetails {

    User user;

    // Инициализация JWT пользователя
    public JWTUser(User user) {
        this.user = user;
    }

    //  Геттер списка ролей пользователя для использования в системе авторизации
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    }

    //  Геттер пароля пользователя
    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    //  Геттер логина пользователя
    @Override
    public String getUsername() {
        return this.user.getLogin();
    }

    //  Геттер изначального пользователя
    public User getUser() {
        return this.user;
    }
}
