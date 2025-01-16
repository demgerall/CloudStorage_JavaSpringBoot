package ru.netology.CloudStorage_SpringBoot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

// Описание сущности файла из облачного хранилища

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
@Entity
public class CloudFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "created", nullable = false, updatable = false)
    LocalDateTime created;

    @Column(name = "file_name")
    @NotBlank
    String name;

    @Column(name = "file_type", nullable = false)
    String fileType;

    @Column(nullable = false)
    long size;

    @Lob
    @Column(nullable = false)
    byte[] bytes;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "username")
    User owner;

    @PrePersist
    protected void create() {
        this.created = LocalDateTime.now();
    }
}
