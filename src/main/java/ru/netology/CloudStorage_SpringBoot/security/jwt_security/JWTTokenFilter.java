package ru.netology.CloudStorage_SpringBoot.security.jwt_security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.CloudStorage_SpringBoot.security.UserDetailsService;
import ru.netology.CloudStorage_SpringBoot.testServices.JWTBlackListService;

import java.io.IOException;

//  Фильтрация JWT токенов

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    JWTUtils jwtUtils;
    UserDetailsService userDetailsService;
    JWTBlackListService jwtBlackListService;

    //  Конструктор класса
    public JWTTokenFilter(JWTUtils jwtUtils, UserDetailsService userDetailsServiceImpl,
                               JWTBlackListService jwtBlackListService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsServiceImpl;
        this.jwtBlackListService = jwtBlackListService;
    }

    //  Выполнение внутренней фильтрации
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //  Достаем из хедера запроса токен
        //  и создаем переменную с префиксом
        String authInfoFromHeader = request.getHeader("auth-token");
        String tokenPrefix = "Bearer ";

        //  Проверяем есть ли в хедере информация о токене и начинается ли она с нужного префикса
        if (authInfoFromHeader != null && !authInfoFromHeader.isBlank() && authInfoFromHeader.startsWith(tokenPrefix)) {

            //  Убираем префикс из токена
            String jwt = authInfoFromHeader.replace(tokenPrefix, "");

            //  Проверяем есть ли сам токен префикса
            if (jwt.isBlank()) {
                //  Если токен пустой, возвращает статус 502 SC_BAD_REQUEST и сообщение
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Header");
                //  Сохраняем ошибку в логах
                log.error("Invalid JWT Token in Header");

                //  Проверяем не добавлен ли токен в черный список
            } else if (jwtBlackListService.isInBlackList(jwt)) {
                //  Если добавлен - возвращаем статус 401 SC_UNAUTHORIZED и сообщение
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be authorized");
                //  Сохраняем ошибку в логах
                log.error("there is JWT Token in blacklist");

                //  Если с токеном все в порядке
            } else {
                try {
                    //  Пытаемся получить логин пользователя
                    //  (находится в try, потому что может выбросить ошибку в случае неудачи)
                    String username = jwtUtils.validateTokenAndGetUsername(jwt);
                    //  Проверяем есть ли такой пользователь в базе данных и получаем JWT пользователя
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    //  Получаем пароль и роль из базы данных
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                                    userDetails.getAuthorities());
                    //  Если в контексте Security авторизация не пройдена - авторизируем с сохранением токена
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }

                    //  Отлов ошибок JWTVerificationException
                } catch (JWTVerificationException e) {
                    //  Возвращает статус 400 SC_BAD_REQUEST c сообщением
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                    //  Сохраняем в логах ошибку
                    log.error("Invalid JWT Token", e);
                }
            }
        }

        //  Фильтруем запрос и ответ
        filterChain.doFilter(request, response);
    }
}
