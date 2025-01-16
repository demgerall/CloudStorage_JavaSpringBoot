package ru.netology.CloudStorage_SpringBoot.utils.exeptions;

//  Ошибка отсутствия пользователя с таким username

public class UsernameNotFoundException extends RuntimeException {

    //  Конструктор ошибки
    public UsernameNotFoundException(String message) {
        //  Принимаем сообщение ошибки
        super(message);
    }

}
