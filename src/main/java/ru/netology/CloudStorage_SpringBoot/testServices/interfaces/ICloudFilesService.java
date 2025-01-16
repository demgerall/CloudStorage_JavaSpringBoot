package ru.netology.CloudStorage_SpringBoot.testServices.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.netology.CloudStorage_SpringBoot.entities.CloudFile;

import java.io.IOException;
import java.util.List;

// Интерфейс для сервиса по работе с файлами из облачного хранилища

public interface ICloudFilesService {

    CloudFile uploadCloudFile (String authToken, String filename, MultipartFile resource) throws IOException;

    List<CloudFile> getAllCloudFilesForUser(String authToken);

    CloudFile downloadCloudFile(String filename, String authToken);

    void deleteCloudFile(String filename, String authToken);

    void renameCloudFile(String authToken, String currentFileName, String newFileName);
}
