package ru.netology.CloudStorage_SpringBoot.security.jwt_security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

//  Обработка случаев, когда пользователь не авторизован для доступа к защищенному ресурсу

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //  Обрабатывает ошибки неавторизованного доступа,
    //  возвращая статус 401 UNAUTHORIZED
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
