package ru.netology.CloudStorage_SpringBoot.utils.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//  Ошибка отсутствия файла в облачном хранилище у пользователя с таким названием

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error Input Data")
public class MissingCloudFileException extends RuntimeException {

    //  Конструктор ошибки
    public MissingCloudFileException(String message) {
        //  Принимаем сообщение ошибки
        super(message);
    }
}
