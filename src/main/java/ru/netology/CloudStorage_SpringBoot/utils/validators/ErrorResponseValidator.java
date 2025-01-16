package ru.netology.CloudStorage_SpringBoot.utils.validators;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

//  Валидатор для ошибок в запроса

@Component
public class ErrorResponseValidator {

    //  Проверяет результаты на наличие ошибок
    public ResponseEntity<Object> mapValidationService(BindingResult result) {

        //  Проверяет есть ли ошибки
        if (result.hasErrors()) {
            //  Если есть ошибки - создает хэш мапу
            //  Ключ — код ошибки, значение — сообщение об ошибке.
            Map<String, String> errorMap = new HashMap<>();

            //  Перебираются все ошибки и добавляются в errorMap
            for (ObjectError error : result.getAllErrors()) {
                errorMap.put(error.getCode(), error.getDefaultMessage());
            }

            //  Возвращается ResponseEntity с кодом статусом 400 BAD_REQUEST и мапой ошибок.
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        //  Если ошибок нет, возвращает null
        return null;
    }
}
