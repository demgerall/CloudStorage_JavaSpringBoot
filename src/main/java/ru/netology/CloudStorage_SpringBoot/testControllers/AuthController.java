package ru.netology.CloudStorage_SpringBoot.testControllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.netology.CloudStorage_SpringBoot.dtos.requests.LogInRequest;
import ru.netology.CloudStorage_SpringBoot.dtos.requests.SignUpRequest;
import ru.netology.CloudStorage_SpringBoot.dtos.responses.JWTResponse;
import ru.netology.CloudStorage_SpringBoot.entities.JWTBlackListEntity;
import ru.netology.CloudStorage_SpringBoot.entities.User;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTUtils;
import ru.netology.CloudStorage_SpringBoot.testServices.JWTBlackListService;
import ru.netology.CloudStorage_SpringBoot.testServices.UserService;
import ru.netology.CloudStorage_SpringBoot.utils.mappers.UserMapper;
import ru.netology.CloudStorage_SpringBoot.utils.validators.ErrorResponseValidator;
import ru.netology.CloudStorage_SpringBoot.utils.validators.UserValidator;

// Контроллер для работы авторизации

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("permitAll()")
@CrossOrigin
public class AuthController {

    AuthenticationManager authenticationManager;
    JWTUtils jwtUtils;
    UserService userService;
    ErrorResponseValidator errorResponseValidator;
    UserMapper userMapper;
    UserValidator userValidator;
    JWTBlackListService jwtBlackListService;

    //  Определение метода входа пользователя по эндпоинту /login и POST запросу
    @PostMapping("/login")
    ResponseEntity<?> userLogIn(@Valid @RequestBody LogInRequest authenticationDTO) {

        //  Создается объект UsernamePasswordAuthenticationToken, содержащий логин и пароль пользователя.
        //  Этот объект используется для аутентификации пользователя.
        var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDTO.getLogin(),
                authenticationDTO.getPassword());

        //  Проводим аутентификацию полученных данных
        authenticationManager.authenticate(authenticationToken);

        //  Генерируем токен для пользователя
        String token = jwtUtils.generateJWTToken(authenticationDTO.getLogin());

        //  Возвращаем ответ со статусом 200 OK и созданным токеном
        return ResponseEntity.ok(new JWTResponse(true, token));
    }

    //  Определение метода выхода пользователя по эндпоинту /logout и POST запросу
    @PostMapping("/logout")
    public ResponseEntity<String> userLogOut(HttpServletRequest request, HttpServletResponse response,
                                         @RequestHeader("auth-token") String authToken) {

        //  Извлекается JWT из заголовка запроса.
        //  Предполагается, что токен начинается с префикса "Bearer " (7 символов).
        String jwt = authToken.substring(7);

        //  JWT токен сохраняется в список черных токенов
        JWTBlackListEntity blackListToken = jwtBlackListService.saveInBlackList(jwt);

        //  Проверка на успешное добавление токена в черный список
        if (blackListToken == null) {
            //  Если неудачно - сохранение действия в логах
            log.error("Something went wrong. Logout failed");
            //  Возвращаем ответ со статусом 500 INTERNAL_SERVER_ERROR и сообщением
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //  Получение текущего объекта аутентификации из контекста безопасности Spring Security.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //  Если объект аутентификации существует,
        //  то используется SecurityContextLogoutHandler для выполнения стандартной процедуры выхода из системы Spring Security,
        //  удаляя информацию об аутентификации из контекста.
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        //  Возвращаем ответ со статусом 200 OK и сообщением
        return ResponseEntity.ok("Successfully logout");
    }

    //  Определение метода регистрации пользователя по эндпоинту /signup и POST запросу
    @PostMapping("/signup")
    public ResponseEntity<Object> userSignUp(@Valid @RequestBody SignUpRequest signUpRequest,
                                               BindingResult bindingResult) {

        //  Преобразует объект SignUpRequest в объект User с помощью UserMapper.
        User user = userMapper.convertToSignupUser(signUpRequest);

        //  Используем валидатор для полученного пользователя
        userValidator.validate(user, bindingResult);

        //  Проверяет bindingResult на наличие ошибок валидации.
        //  Если ошибки есть, ErrorResponseValidator.mapValidationService создает ResponseEntity с описанием ошибок
        ResponseEntity<Object> errors = errorResponseValidator.mapValidationService(bindingResult);

        //  Если есть ошибки в errors возвращает их
        if (errors != null) {
            return errors;
        }

        //  Регистрирует пользователя
        userService.registration(user);

        //  Возвращаем ответ со статусом 200 OK и сообщением
        return ResponseEntity.ok("User successfully registered");
    }
}
