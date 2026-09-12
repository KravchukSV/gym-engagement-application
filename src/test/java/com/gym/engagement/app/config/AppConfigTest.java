package com.gym.engagement.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.engagement.app.dto.TraineeDto;
import com.gym.engagement.app.dto.TrainerDto;
import com.gym.engagement.app.dto.TrainingDto;
import com.gym.engagement.app.dto.TrainingTypeDto;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppConfigTest {

    private static final Long ID = 1L;
    private static final String FIRST_NAME = "Sam";
    private static final String LAST_NAME = "Serious";
    private static final String USERNAME = "Sam.Serious";
    private static final String PASSWORD = "password";
    private static final Boolean IS_ACTIVE = true;
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 1, 1);
    private static final String ADDRESS = "123 Funny St";
    private static final String TRAINING_TYPE_NAME = "Cardio";
    private static final String TRAINING_NAME = "Morning Cardio";

    private ModelMapper mapper;

    @BeforeEach
    void setUp() {
        AppConfig appConfig = new AppConfig();
        mapper = appConfig.modelMapper();
    }

    @Test
    @DisplayName("Should load application context and register all required beans")
    void shouldLoadApplicationContextFromAppConfig() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertNotNull(context.getBean(AppConfig.class));
            assertNotNull(context.getBean(ObjectMapper.class));
            assertNotNull(context.getBean(ModelMapper.class));
            assertNotNull(context.getBean(PasswordEncoder.class));
        }
    }

    @Test
    @DisplayName("Should map TraineeDto to Trainee entity correctly")
    void shouldMapTraineeDtoToTrainee() {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUserId(ID);
        traineeDto.setFirstName(FIRST_NAME);
        traineeDto.setLastName(LAST_NAME);
        traineeDto.setUsername(USERNAME);
        traineeDto.setPassword(PASSWORD);
        traineeDto.setIsActive(IS_ACTIVE);
        traineeDto.setDateOfBirth(DATE_OF_BIRTH);
        traineeDto.setAddress(ADDRESS);

        Trainee actual = mapper.map(traineeDto, Trainee.class);

        assertNotNull(actual);
        assertEquals(traineeDto.getUserId(), actual.getUserId());
        assertEquals(traineeDto.getFirstName(), actual.getFirstName());
        assertEquals(traineeDto.getLastName(), actual.getLastName());
        assertEquals(traineeDto.getUsername(), actual.getUsername());
        assertEquals(traineeDto.getPassword(), actual.getPassword());
        assertTrue(actual.getIsActive());
        assertEquals(traineeDto.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(traineeDto.getAddress(), actual.getAddress());
    }

    @Test
    @DisplayName("Should map TrainerDto with Specialization to Trainer entity correctly")
    void shouldMapTrainerDtoToTrainer() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUserId(ID);
        trainerDto.setFirstName(FIRST_NAME);
        trainerDto.setLastName(LAST_NAME);
        trainerDto.setUsername(USERNAME);
        trainerDto.setPassword(PASSWORD);
        trainerDto.setIsActive(IS_ACTIVE);
        trainerDto.setSpecialization(null);

        Trainer actual = mapper.map(trainerDto, Trainer.class);

        assertNotNull(actual);
        assertEquals(trainerDto.getUserId(), actual.getUserId());
        assertEquals(trainerDto.getFirstName(), actual.getFirstName());
        assertEquals(trainerDto.getLastName(), actual.getLastName());
        assertEquals(trainerDto.getUsername(), actual.getUsername());
        assertEquals(trainerDto.getPassword(), actual.getPassword());
        assertTrue(actual.getIsActive());
        assertNull(actual.getSpecialization());
    }

    @Test
    @DisplayName("Should map TrainingDto to Training entity correctly")
    void shouldMapTrainingDtoToTraining() {
        TrainingTypeDto dto = new TrainingTypeDto();
        dto.setTrainingTypeId(ID);
        dto.setTrainingTypeName(TRAINING_TYPE_NAME);

        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTrainingId(ID);
        trainingDto.setTrainingName(TRAINING_NAME);
        trainingDto.setTrainingType(dto);

        Training actual = mapper.map(trainingDto, Training.class);

        assertNotNull(actual);
        assertEquals(trainingDto.getTrainingId(), actual.getTrainingId());
        assertEquals(trainingDto.getTraineeId(), actual.getTraineeId());
        assertEquals(trainingDto.getTrainerId(), actual.getTrainerId());
        assertEquals(trainingDto.getTrainingName(), actual.getTrainingName());
        assertEquals(trainingDto.getTrainingDate(), actual.getTrainingDate());
        assertEquals(trainingDto.getTrainingDuration(), actual.getTrainingDuration());
        assertNotNull(actual.getTrainingType());
        assertEquals(ID, actual.getTrainingType().getTrainingTypeId());
        assertEquals(TRAINING_TYPE_NAME, actual.getTrainingType().getTrainingTypeName());
    }
}