package ru.netology.CloudStorage_SpringBoot.testControllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.CloudStorage_SpringBoot.dtos.entities.CloudFileDTO;
import ru.netology.CloudStorage_SpringBoot.entities.CloudFile;
import ru.netology.CloudStorage_SpringBoot.testServices.CloudFilesService;
import ru.netology.CloudStorage_SpringBoot.utils.mappers.CloudFileMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//  Контроллер для работы с файлами из облачного хранилища

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudFileController {

    CloudFilesService cloudFilesService;
    CloudFileMapper cloudFileMapper;

    //  Определение метода загрузки файла в облачное хранилище по эндпоинту /file и POST запросу
    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadCloudFile(@RequestHeader("auth-token") String authToken,
                                                  @RequestParam("filename") String fileName, MultipartFile file) throws IOException {

        //  Обращение к сервису CloudFilesService и вызов его метода uploadCloudFile
        CloudFile cloudFile = cloudFilesService.uploadCloudFile(authToken, fileName, file);

        //  Перевод полученного объекта файла в DTO
        CloudFileDTO cloudFileDTO = cloudFileMapper.fromCloudFileToDTO(cloudFile);

        //  Возвращаем как ответ переведенный файл и статус 200 OK
        return new ResponseEntity<>(cloudFileDTO, HttpStatus.OK);
    }

    //  Определение метода изменения имени файла в облачном хранилище по эндпоинту /file и PUT запросу
    @PutMapping("/file")
    public ResponseEntity<String> editCloudFileName(@RequestHeader("auth-token") String authToken,
                                                    @RequestParam("filename") String oldFileName,
                                                    @RequestBody String newFileName) {

        //  Обращение к сервису CloudFilesService и вызов его метода renameCloudFile
        cloudFilesService.renameCloudFile(authToken, oldFileName, newFileName);

        //  Возвращаем как ответ сообщение и статус 200 OK
        return new ResponseEntity<>("Success rename", HttpStatus.OK);
    }

    //  Определение метода выгрузки файла из облачного хранилища по эндпоинту /file и GET запросу
    @GetMapping("/file")
    public ResponseEntity<?> downloadCloudFile(@RequestParam("filename") String fileName,
                                               @RequestHeader("auth-token") String authToken) {

        //  Обращение к сервису CloudFilesService и вызов его метода renameCloudFile.
        //  В ответ получаем сам файл из облачного хранилища
        CloudFile cloudFile = cloudFilesService.downloadCloudFile(fileName, authToken);

        //  Проверяем не пустой ли ответ
        if (cloudFile != null) {
            //  Если не пустой возвращаем ответ со статусом 200 OK и файлом
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + cloudFile.getName())
                    .body(cloudFile);
        } else {
            //  Иначе возвращаем статус 404 NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //  Определение метода удаление файла из облачного хранилища по эндпоинту /file и DELETE запросу
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteCloudFile(@RequestParam("filename") String fileName,
                                             @RequestHeader("auth-token") String authToken) {

        //  Обращение к сервису CloudFilesService и вызов его метода deleteCloudFile.
        cloudFilesService.deleteCloudFile(fileName, authToken);

        //  Возвращаем как ответ сообщение и статус 200 OK
        return ResponseEntity.ok("Successfully deleted");
    }

    //  Определение метода выгрузки всех файлов из облачного хранилища по эндпоинту /list и GET запросу
    @GetMapping("/list")
    public ResponseEntity<?> getAllCloudFiles(@RequestHeader("auth-token") String authToken,
                                              @RequestParam("limit") int limit) {

        //  Проверяем не отправил ли пользователь отрицательное или нулевое значение лимита
        if (limit <= 0) {
            //  Если значение не подходящее, возвращаем статус 400 BAD_REQUEST и сообщение
            return ResponseEntity.badRequest().body("Error input data");
        }

        //  Обращение к сервису CloudFilesService и вызов его метода getAllCloudFilesForUser.
        //  В ответ получаем массив файлов из облачного хранилища,
        //  также переводим файлы в DTO
        List<CloudFileDTO> cloudFileDTOs = cloudFilesService.getAllCloudFilesForUser(authToken)
                .stream()
                .limit(limit)
                .map(cloudFileMapper::fromCloudFileToDTO)
                .collect(Collectors.toList());

        //  Проверяем размер полученного массива
        if (cloudFileDTOs.size() > 3) {
            //  Если проверку не проходит, то возвращаем статус 500 Internal_Server_Error
            return ResponseEntity.internalServerError().body("Error getting file list");
        }

        //  Возвращаем статус 200 OK и массив файлов
        return ResponseEntity.ok(cloudFileDTOs);
    }
}
