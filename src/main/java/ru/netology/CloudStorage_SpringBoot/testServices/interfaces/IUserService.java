package ru.netology.CloudStorage_SpringBoot.testServices.interfaces;

import ru.netology.CloudStorage_SpringBoot.entities.User;

//  Интерфейс для сервиса по работе с пользователями

public interface IUserService {

    void registration(User user);

    User findByUsername(String username);
}
