package ru.netology.CloudStorage_SpringBoot.testServices.interfaces;

import ru.netology.CloudStorage_SpringBoot.entities.JWTBlackListEntity;

//  Интерфейс для сервиса по работе с черным списком JWT Tokens

public interface IJWTBlackListService {

    boolean isInBlackList(String jwt);

    JWTBlackListEntity saveInBlackList(String jwt);
}
