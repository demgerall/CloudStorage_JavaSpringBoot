package ru.netology.CloudStorage_SpringBoot.testServices;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.CloudStorage_SpringBoot.entities.Role;
import ru.netology.CloudStorage_SpringBoot.entities.User;
import ru.netology.CloudStorage_SpringBoot.repositories.UserRepository;
import ru.netology.CloudStorage_SpringBoot.testServices.interfaces.IUserService;

//  Сервис для работы с пользователями

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class UserService implements IUserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    //  Инициализация сервиса (конструктор)
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //  Регистрация пользователя
    @Transactional
    public void registration(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }

    //  Поиск пользователя по логину
    public User findByUsername(String username) {
        //  Возвращает пользователя по логину, если такой найден,
        //  иначе возвращает null
        return userRepository.findByLogin(username)
                .orElse(null);
    }
}
