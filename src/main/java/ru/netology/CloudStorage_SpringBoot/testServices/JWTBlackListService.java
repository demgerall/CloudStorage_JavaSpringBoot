package ru.netology.CloudStorage_SpringBoot.testServices;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netology.CloudStorage_SpringBoot.entities.JWTBlackListEntity;
import ru.netology.CloudStorage_SpringBoot.repositories.JWTBlackListRepository;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTUtils;
import ru.netology.CloudStorage_SpringBoot.testServices.interfaces.IJWTBlackListService;

//  Сервис по работе с черным списком JWT Tokens

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JWTBlackListService implements IJWTBlackListService {

    JWTBlackListRepository jwtBlackListRepository;
    JWTUtils jwtUtils;

    //  Конструктор сервиса
    @Autowired
    public JWTBlackListService(JWTBlackListRepository jwtBlackListRepository, JWTUtils jwtUtils) {
        this.jwtBlackListRepository = jwtBlackListRepository;
        this.jwtUtils = jwtUtils;
    }

    //  Проверяет, есть ли JWT токен в черном списке
    @Override
    public boolean isInBlackList(String jwt) {
        return jwtBlackListRepository.findByJWTEquals(jwt).isPresent();
    }

    //  Сохраняет в черный список JWT токен
    @Override
    public JWTBlackListEntity saveInBlackList(String jwt) {
        Long exp = jwtUtils.extractExpiration(jwt).getTime();

        return jwtBlackListRepository.saveAndFlush(new JWTBlackListEntity(jwt, exp));
    }
}
