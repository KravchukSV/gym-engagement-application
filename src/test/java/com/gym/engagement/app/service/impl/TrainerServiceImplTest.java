package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.TrainingType;
import com.gym.engagement.app.service.common.CredentialsGenerator;
import com.gym.engagement.app.service.common.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    private static final Long TRAINER_ID = 1L;
    private static final String FIRST_NAME = "Andrii";
    private static final String LAST_NAME = "Melnyk";
    private static final String GENERATED_USERNAME = FIRST_NAME + "." + LAST_NAME;
    private static final String GENERATED_PASSWORD = "Password123";

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private CredentialsGenerator credentialsGenerator;

    @Mock
    private EntityValidator entityValidator;

    @InjectMocks
    private TrainerServiceImpl service;

    private Trainer sampleTrainer;

    @BeforeEach
    void setUp() {
        sampleTrainer = createTrainerBuilder().build();
    }

    @Test
    @DisplayName("createTrainer() should validate, generate credentials, and save trainer")
    void createTrainer_ShouldGenerateCredentialsAndSave() {
        when(credentialsGenerator.generateUsername(FIRST_NAME, LAST_NAME)).thenReturn(GENERATED_USERNAME);
        when(credentialsGenerator.generatePassword()).thenReturn(GENERATED_PASSWORD);
        when(trainerDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainer actual = service.createTrainer(sampleTrainer);

        verify(entityValidator).validateUserForCreation(sampleTrainer);
        verify(credentialsGenerator).generateUsername(FIRST_NAME, LAST_NAME);
        verify(credentialsGenerator).generatePassword();
        verify(trainerDao).save(any(Trainer.class));
        assertEquals(GENERATED_USERNAME, actual.getUsername());
        assertEquals(GENERATED_PASSWORD, actual.getPassword());
    }

    @Test
    @DisplayName("updateTrainer() should validate and update trainer when found")
    void updateTrainer_ShouldUpdate_WhenFound() {
        Trainer trainerToUpdate = createTrainerBuilder()
                .userId(TRAINER_ID)
                .build();

        when(trainerDao.update(TRAINER_ID, trainerToUpdate)).thenReturn(Optional.of(trainerToUpdate));

        Trainer actual = service.updateTrainer(trainerToUpdate);

        verify(entityValidator).validateUserForUpdate(trainerToUpdate);
        assertEquals(trainerToUpdate, actual);
    }

    @Test
    @DisplayName("updateTrainer() should throw Exception when trainer is not found")
    void updateTrainer_ShouldThrowException_WhenNotFound() {
        Trainer trainerToUpdate = createTrainerBuilder()
                .userId(TRAINER_ID)
                .build();

        when(trainerDao.update(TRAINER_ID, trainerToUpdate)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.updateTrainer(trainerToUpdate));
        verify(entityValidator).validateUserForUpdate(trainerToUpdate);
    }

    @Test
    @DisplayName("findTrainerById() should validate ID and return Optional Trainer")
    void findTrainerById_ShouldReturnTrainer() {
        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.of(sampleTrainer));

        Optional<Trainer> actual = service.findTrainerById(TRAINER_ID);

        verify(entityValidator).validateId(TRAINER_ID);
        assertTrue(actual.isPresent());
        assertEquals(sampleTrainer, actual.get());
    }

    @Test
    @DisplayName("findAllTrainers() should return list of trainers")
    void findAllTrainers_ShouldReturnList() {
        when(trainerDao.findAll()).thenReturn(List.of(sampleTrainer));

        List<Trainer> actual = service.findAllTrainers();

        assertEquals(1, actual.size());
        assertEquals(sampleTrainer, actual.getFirst());
    }

    @Test
    @DisplayName("createTrainer() should throw Exception when validation fails")
    void createTrainer_ShouldThrowException_WhenValidationFails() {
        doThrow(new IllegalArgumentException("Invalid user"))
                .when(entityValidator)
                .validateUserForCreation(sampleTrainer);

        assertThrows(IllegalArgumentException.class, () -> service.createTrainer(sampleTrainer));
    }

    private Trainer.TrainerBuilder<?, ?> createTrainerBuilder() {
        return Trainer.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .specialization(TrainingType.builder()
                                .trainingTypeName("Powerlifting")
                                .build());
    }
}