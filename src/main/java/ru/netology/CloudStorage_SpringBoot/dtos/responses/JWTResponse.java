package ru.netology.CloudStorage_SpringBoot.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

//  Описание ответа с JWT токеном

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JWTResponse {
    boolean success;
    @JsonProperty("auth-token")
    String authToken;
}
