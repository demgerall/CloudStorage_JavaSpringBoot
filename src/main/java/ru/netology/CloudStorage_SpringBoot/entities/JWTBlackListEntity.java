package ru.netology.CloudStorage_SpringBoot.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

//  Описание сущности черного списка JWT Tokens

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "black_list_jwt")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTBlackListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String jwt;

    @Column(nullable = false)
    Long exp;

    public JWTBlackListEntity(String jwt, Long exp) {
        this.jwt = jwt;
        this.exp = exp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JWTBlackListEntity jwtBlackListEntity = (JWTBlackListEntity) o;
        return jwt.equals(jwtBlackListEntity.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt);
    }
}
