package ru.netology.CloudStorage_SpringBoot.utils.validators;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.netology.CloudStorage_SpringBoot.entities.User;
import ru.netology.CloudStorage_SpringBoot.testServices.UserService;

//  Валидатор, который проверяет пользователей

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserValidator implements Validator {

    UserService userService;

    //  Инициализация сервиса пользователей (конструктор)
    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    //  Проверяет получаемый класс на соответствие классу User
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    //  Проверяет, существует ли пользователь с таким же именем пользователя
    @Override
    public void validate(Object target, Errors errors) {

        //  Приводим полученный объект к объекту User
        User user = (User) target;

        //  Ищем пользователя с таким же логином
        User userExist = userService.findByUsername(user.getLogin());

        //  Если такой пользователь находится, возвращаем ошибку 400
        if(userExist != null) {
            errors.rejectValue("login", "400","A user with the same username already exists");
        }

    }
}
