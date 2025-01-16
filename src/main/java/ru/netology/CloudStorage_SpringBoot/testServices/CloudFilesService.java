package ru.netology.CloudStorage_SpringBoot.testServices;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.CloudStorage_SpringBoot.entities.CloudFile;
import ru.netology.CloudStorage_SpringBoot.entities.User;
import ru.netology.CloudStorage_SpringBoot.repositories.CloudFilesRepository;
import ru.netology.CloudStorage_SpringBoot.repositories.UserRepository;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTUtils;
import ru.netology.CloudStorage_SpringBoot.testServices.interfaces.ICloudFilesService;
import ru.netology.CloudStorage_SpringBoot.utils.exeptions.MissingCloudFileException;
import ru.netology.CloudStorage_SpringBoot.utils.exeptions.UsernameNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Сервис по работе с файлами из облачного хранилища

@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CloudFilesService implements ICloudFilesService {

    final CloudFilesRepository cloudFilesRepository;
    final UserRepository userRepository;
    final JWTUtils jwtUtils;

    //  Инициализация сервиса (конструктор)
    @Autowired
    public CloudFilesService(CloudFilesRepository cloudFilesRepository, UserRepository userRepository, JWTUtils jwtUtils) {
        this.cloudFilesRepository = cloudFilesRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    //  Загрузка файла в облачное хранилище
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CloudFile uploadCloudFile(String authToken, String fileName, MultipartFile resource) throws IOException {

        //  Проверяем валидность токена и достаем пользователя
        User user = getUserByToken(authToken);

        //  Проверяем, что с файлом (данными) все хорошо (они не пустые)
        if (resource.isEmpty()) {
            // Если пустые - сохраняем действие в логах
            log.info("the file not found");
            //  Выкидываем ошибку MissingCloudFileException
            throw new MissingCloudFileException("Error input data");
        }

        //  Пробуем найти в облачном хранилище файл с таким же названием
        Optional<CloudFile> checkFilename = cloudFilesRepository.findCloudFileByName(fileName);

        //  Проверяем нашелся ли
        if(checkFilename.isPresent()) {
            //  Если нашелся - сохраняем действие в логах
            log.error("A file with the same name ({}) already exists", fileName);
            //  Выкидываем ошибку MissingCloudFileException
            throw new MissingCloudFileException("A file with the same name already exists");
        }

        //  Создаем сущность файла с помощью билдера
        CloudFile file = CloudFile.builder()
                .name(fileName)
                .fileType(resource.getContentType())
                .size(resource.getSize())
                .bytes(resource.getBytes())
                .owner(user)
                .build();

        //  Сохраняем файл в репозитории облачного хранилища
        cloudFilesRepository.save(file);

        //  Сохраняем действие в логах
        log.info("User successfully uploaded a file {}", fileName);

        //  Возвращаем сам созданную сущность файла
        return file;
    }

    //  Выгрузка файла из облачного хранилища
    @Override
    public CloudFile downloadCloudFile(String fileName, String authToken) {

        //  Проверяем валидность токена и достаем пользователя
        User user = getUserByToken(authToken);

        //  Ищем файл в облачном хранилище по названию и пользователю,
        //  иначе выкидываем ошибку MissingCloudFileException
        return cloudFilesRepository.findCloudFileByNameAndOwner(fileName, user)
                .orElseThrow(() -> new MissingCloudFileException("The file couldn't be found"));
    }

    //  Удаление файла по названию из облачного хранилища
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCloudFile(String fileName, String authToken) {

        //  Ищем файл из облачного хранилища по названию с помощью метода getCloudFileByName
        CloudFile cloudFile = getCloudFileByName(fileName, authToken);

        //  Удаляем файл по id из репозитория
        cloudFilesRepository.deleteById(cloudFile.getId());

        //  Сохраняем действие в логах
        log.info("File {} was successfully deleted", fileName);
    }

    //  Переименование файла по названию в облачном хранилище
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void renameCloudFile(String authToken, String currentFileName, String newFileName) {

        //  Ищем файл из облачного хранилища по названию с помощью метода getCloudFileByName
        CloudFile file = getCloudFileByName(currentFileName, authToken);

        //  Устанавливаем для файла новое имя
        file.setName(newFileName);

        //  Сохраняем действие в логах
        log.info("File's name was successfully changed to {}", newFileName);

        //  Сохраняем изменения для файла в репозитории
        cloudFilesRepository.saveAndFlush(file);
    }

    //  Ищем все файлы для пользователя
    @Override
    public List<CloudFile> getAllCloudFilesForUser(String authToken) {

        //  Проверяем валидность токена и достаем пользователя
        User user = getUserByToken(authToken);

        //  Находим и возвращаем все файлы для пользователя
        return cloudFilesRepository.findAllByOwnerOrderByCreatedDesc(user);
    }

    //  Ищем юзера по токену
    private User getUserByToken(String authToken) {

        String tokenPrefix = "Bearer ";

        //  Проверяем валидность токена и достаем имя пользователя
        String username = jwtUtils.validateTokenAndGetUsername(authToken.replace(tokenPrefix, ""));

        //  Ищем в репозитории пользователей пользователя и возвращаем его,
        //  иначе выкидываем ошибку UsernameNotFoundException
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    //  Ищем файл из облачного хранилища по названию
    private CloudFile getCloudFileByName(String filename, String authToken) {

        //  Проверяем валидность токена и достаем пользователя
        User user = getUserByToken(authToken);

        //  Ищем в репозитории файлов файл по названию и пользователю и возвращаем его,
        //  иначе выкидываем ошибку CloudFileException
        return cloudFilesRepository.findCloudFileByNameAndOwner(filename, user)
                .orElseThrow(() -> new MissingCloudFileException("File can't be found for user " + user.getLogin()));
    }
}
