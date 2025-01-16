package ru.netology.CloudStorage_SpringBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import ru.netology.CloudStorage_SpringBoot.entities.CloudFile;
import ru.netology.CloudStorage_SpringBoot.entities.User;

//  Интерфейс репозитория для файлов с облачного хранилища

@Repository
public interface CloudFilesRepository extends JpaRepository<CloudFile, Long> {

    //  Поиск всех файлов из облачного хранилища по пользователю + сортировка по дате
    List<CloudFile> findAllByOwnerOrderByCreatedDesc(User user);

    //  Поиск файла из облачного хранилища по названию
    Optional<CloudFile> findCloudFileByName(String name);

    //  Поиск файла из облачного хранилища по названию файла и пользователю
    Optional<CloudFile> findCloudFileByNameAndOwner(String filename, User user);
}
