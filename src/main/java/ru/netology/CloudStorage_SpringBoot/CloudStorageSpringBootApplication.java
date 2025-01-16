package ru.netology.CloudStorage_SpringBoot;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// Точка запуска нашего Spring Boot приложения

@SpringBootApplication
public class CloudStorageSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudStorageSpringBootApplication.class, args);
	}

	//	Cоздаем биновую зависимость для объекта ModelMapper
	//	ModelMapper – это библиотека для маппинга объектов, то есть преобразования объектов одного типа в объекты другого типа.
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
