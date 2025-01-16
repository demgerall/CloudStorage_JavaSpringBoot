package ru.netology.CloudStorage_SpringBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.CloudStorage_SpringBoot.entities.JWTBlackListEntity;

import java.util.Optional;

//  Интерфейс репозитория для черного списка JWT Tokens

@Repository
public interface JWTBlackListRepository extends JpaRepository<JWTBlackListEntity, Long> {

    //  Поиск одинаковых JWT Tokens
    Optional<JWTBlackListEntity> findByJWTEquals(String jwt);
}
