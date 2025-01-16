package ru.netology.CloudStorage_SpringBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.CloudStorage_SpringBoot.entities.User;

import java.util.Optional;

//  Интерфейс репозитория для пользователей

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //  Поиск пользователя по логину
    Optional<User> findByLogin(String login);
}
