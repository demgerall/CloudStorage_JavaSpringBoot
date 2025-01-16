package ru.netology.CloudStorage_SpringBoot.testControllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.netology.CloudStorage_SpringBoot.utils.exeptions.MissingCloudFileException;
import ru.netology.CloudStorage_SpringBoot.utils.exeptions.UsernameNotFoundException;

//  Контроллер для работы с ошибками

@RestControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {

    //  Ловит ошибки BadCredentialsException и UsernameNotFoundException.
    //  Затем возвращает статус 400 BAD_REQUEST с сообщением
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Object> UserBadCredentialsExceptions() {
        return ResponseEntity.badRequest().body("Bad credentials");
    }

    //  Ловит ошибку MissingCloudFileException.
    //  Затем возвращает статус 400 BAD_REQUEST с сообщением
    @ExceptionHandler({MissingCloudFileException.class})
    public ResponseEntity<Object> MissingCloudFileException(MissingCloudFileException ex) {
        return ResponseEntity.badRequest().body("Error input data");
    }
}
