package ru.netology.CloudStorage_SpringBoot.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.netology.CloudStorage_SpringBoot.entities.User;
import ru.netology.CloudStorage_SpringBoot.repositories.UserRepository;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTUser;
import ru.netology.CloudStorage_SpringBoot.utils.exeptions.UsernameNotFoundException;

//  Сервис, который ищет пользователя и переводит его в JWT пользователя для Security

@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    UserRepository userRepository;

    //  Конструктор сервиса
    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //  Поиск пользователя по логину и преобразование его в JWT пользователя
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        //  Поиск пользователя по логину,
        //  если пользователя с таким логином нет, выбрасывает ошибку UsernameNotFoundException
        User user = userRepository.findByLogin(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));

        //  Сохраняем действие в логах
        log.info("User {} is successfully loaded in the function loadUserByUsername", userName);

        //  Возвращает JWT пользователя
        return new JWTUser(user);
    }
}
