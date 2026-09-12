package com.gym.engagement.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gym.engagement.app.dto.TraineeDto;
import com.gym.engagement.app.dto.TrainerDto;
import com.gym.engagement.app.dto.TrainingDto;
import com.gym.engagement.app.dto.TrainingTypeDto;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.TrainingType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
@ComponentScan(basePackages = {"com.gym.engagement.app"})
@PropertySource(value = "classpath:application.properties")
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        configureTraineeMapping(modelMapper);
        configureTrainingTypeMapping(modelMapper);
        configureTrainerMapping(modelMapper);
        configureTrainingMapping(modelMapper);

        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void configureTraineeMapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(TraineeDto.class, Trainee.class)
                .setProvider(request -> {
                    TraineeDto dto = (TraineeDto) request.getSource();

                    return Trainee.builder()
                            .userId(dto.getUserId())
                            .firstName(dto.getFirstName())
                            .lastName(dto.getLastName())
                            .username(dto.getUsername())
                            .password(dto.getPassword())
                            .isActive(dto.getIsActive())
                            .dateOfBirth(dto.getDateOfBirth())
                            .address(dto.getAddress())
                            .build();
                });
    }

    private void configureTrainingTypeMapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(TrainingTypeDto.class, TrainingType.class)
                .setProvider(request -> {
                    TrainingTypeDto dto = (TrainingTypeDto) request.getSource();

                    return TrainingType.builder()
                            .trainingTypeId(dto.getTrainingTypeId())
                            .trainingTypeName(dto.getTrainingTypeName())
                            .build();
                });
    }

    private void configureTrainingMapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(TrainingDto.class, Training.class)
                .setProvider(request -> {
                    TrainingDto dto = (TrainingDto) request.getSource();

                    return Training.builder()
                            .trainingId(dto.getTrainingId())
                            .traineeId(dto.getTraineeId())
                            .trainerId(dto.getTrainerId())
                            .trainingName(dto.getTrainingName())
                            .trainingType(mapTrainingType(modelMapper, dto.getTrainingType()))
                            .trainingDate(dto.getTrainingDate())
                            .trainingDuration(dto.getTrainingDuration())
                            .build();
                });
    }

    private void configureTrainerMapping(ModelMapper modelMapper) {
        modelMapper.createTypeMap(TrainerDto.class, Trainer.class)
                .setProvider(request -> {
                    TrainerDto dto = (TrainerDto) request.getSource();

                    return Trainer.builder()
                            .userId(dto.getUserId())
                            .firstName(dto.getFirstName())
                            .lastName(dto.getLastName())
                            .username(dto.getUsername())
                            .password(dto.getPassword())
                            .isActive(dto.getIsActive())
                            .specialization(mapTrainingType(modelMapper, dto.getSpecialization()))
                            .build();
                });
    }

    private TrainingType mapTrainingType(ModelMapper modelMapper, TrainingTypeDto dto) {
        if (dto == null) {
            return null;
        }

        return modelMapper.map(dto, TrainingType.class);
    }
}