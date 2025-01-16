package ru.netology.CloudStorage_SpringBoot.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

// Описание сущности пользователя

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "The username field must not be empty.")
    @Size(min = 6, max = 20, message = "Username length must be between 6 and 20 characters")
    @Column(unique = true, name = "username")
    String login;

    @NotBlank
    @Size(min = 8, max = 60, message = "Password length must be between 8 and 60 characters")
    @Column(nullable = false)
    String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    Role role;

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
