package ru.netology.CloudStorage_SpringBoot.utils.mappers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.netology.CloudStorage_SpringBoot.dtos.requests.SignUpRequest;
import ru.netology.CloudStorage_SpringBoot.entities.User;

//  Преобразует объект SignUpRequest в объект User

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper {

    ModelMapper modelMapper;

    //  Инициализация ModelMapper
    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //  Преобразует данные регистрирующегося пользователя в зарегистрированного
    public User convertToSignupUser(SignUpRequest request) {
        return this.modelMapper.map(request, User.class);
    }
}
