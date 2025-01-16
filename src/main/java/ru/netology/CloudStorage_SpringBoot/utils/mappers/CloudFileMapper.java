package ru.netology.CloudStorage_SpringBoot.utils.mappers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.netology.CloudStorage_SpringBoot.dtos.entities.CloudFileDTO;
import ru.netology.CloudStorage_SpringBoot.entities.CloudFile;

// Преобразует объект CloudFile в объект CloudFileDTO

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudFileMapper {

    ModelMapper modelMapper;

    //  Инициализация ModelMapper
    @Autowired
    public CloudFileMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Перевод облачного файла в DTO
    public CloudFileDTO fromCloudFileToDTO(CloudFile file) {
        return this.modelMapper.map(file, CloudFileDTO.class);
    }
}
