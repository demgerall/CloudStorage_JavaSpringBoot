package ru.netology.CloudStorage_SpringBoot.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

//  Описание запроса на регистрацию пользователя

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest implements Serializable {

    @NotEmpty(message = "The username field must not be empty.")
    @Size(min=6, max=20, message = "Username length must be between 6 and 20 characters")
    String login;

    @NotEmpty(message = "The username field must not be empty.")
    @Size(min=8, max=60, message = "Password length must be between 8 and 60 characters")
    String password;
}
