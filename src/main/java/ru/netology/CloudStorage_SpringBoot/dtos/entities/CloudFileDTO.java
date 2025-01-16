package ru.netology.CloudStorage_SpringBoot.dtos.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.io.Serializable;

// Объект CloudFile для передачи между подсистемами приложения

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@EqualsAndHashCode
@Component
public class CloudFileDTO implements Serializable, Comparable<CloudFileDTO> {

    @JsonProperty(value = "filename")
    String fileName;

    @EqualsAndHashCode.Exclude
    int size;

    @Override
    public int compareTo(CloudFileDTO o) {
        return this.getFileName().compareTo(o.getFileName());
    }
}
