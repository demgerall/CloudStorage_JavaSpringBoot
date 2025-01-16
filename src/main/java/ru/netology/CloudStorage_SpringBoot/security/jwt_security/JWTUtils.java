package ru.netology.CloudStorage_SpringBoot.security.jwt_security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

//  Генерация JWT Tokens и их проверка

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class JWTUtils {

    @Value("${app.jwtSecret}")
    String jwtSecret;

    //  Время до истечения срока действия в минутах
    @Value("${app.jwtExpiration}")
    Long jwtExpirationInMinutes;

    @Value("User details")
    String subject;

    @Value("Spring-boot app 'cloudService")
    String issuer;

    //  Генерирует JWT токен
    public String generateJWTToken(String username) {

        //  Создание даты, когда токен протухнет
        Date validity = Date.from(ZonedDateTime.now().plusMinutes(jwtExpirationInMinutes).toInstant());

        //  Возвращает сгенерированный JWT Token
        return JWT.create()
                .withSubject(subject)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    //  Проверяет валидность JWT, используя тот же секретный ключ, субъект и издателя, что и при генерации.
    //  Если токен валиден, извлекает и возвращает имя пользователя из поля username в полезной нагрузке токена.
    //  В случае ошибки валидации выбрасывает исключение JWTVerificationException
    public String validateTokenAndGetUsername(String token) throws JWTVerificationException {

        //  Создание верификатора
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();

        //  Проверка валидности токена
        DecodedJWT jwt = verifier.verify(token);

        //  Возвращает username
        return jwt.getClaim("username").asString();
    }

    //  Проверяет валидность JWT и извлекает из него дату истечения (expiresAt).
    public Date extractExpiration(String token) {

        //  Создание верификатора
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();

        //  Проверка валидности токена
        DecodedJWT jwt = verifier.verify(token);

        //  Возвращает дату, когда токен протухнет
        return jwt.getExpiresAt();
    }
}
