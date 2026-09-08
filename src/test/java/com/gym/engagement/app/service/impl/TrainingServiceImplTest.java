package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TrainingDao;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.service.common.EntityValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    private static final Long TRAINING_ID = 100L;
    private static final Long TRAINEE_ID = 1L;
    private static final Long TRAINER_ID = 2L;
    private static final String TRAINING_NAME = "Sumo Deadlift Session";
    private static final int DURATION = 90;
    private static final Training SAMPLE_TRAINING = createTraining();

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private EntityValidator entityValidator;

    @InjectMocks
    private TrainingServiceImpl service;

    @Test
    @DisplayName("createTraining() should validate and delegate to DAO")
    void createTraining_ShouldValidateAndSave() {
        when(trainingDao.save(SAMPLE_TRAINING)).thenReturn(SAMPLE_TRAINING);

        Training actual = service.createTraining(SAMPLE_TRAINING);

        verify(entityValidator).validateTrainingForCreation(SAMPLE_TRAINING);
        verify(trainingDao).save(SAMPLE_TRAINING);
        assertEquals(SAMPLE_TRAINING, actual);
    }

    @Test
    @DisplayName("findTrainingById() should validate ID and return Optional Training")
    void findTrainingById_ShouldReturnTraining() {
        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.of(SAMPLE_TRAINING));

        Optional<Training> actual = service.findTrainingById(TRAINING_ID);

        verify(entityValidator).validateId(TRAINING_ID);
        assertTrue(actual.isPresent());
        assertEquals(SAMPLE_TRAINING, actual.get());
    }

    @Test
    @DisplayName("findAllTrainings() should return list of trainings")
    void findAllTrainings_ShouldReturnList() {
        when(trainingDao.findAll()).thenReturn(List.of(SAMPLE_TRAINING));

        List<Training> actual = service.findAllTrainings();

        assertEquals(1, actual.size());
        assertEquals(SAMPLE_TRAINING, actual.getFirst());
    }

    @Test
    @DisplayName("createTraining() should throw Exception when validation fails")
    void createTraining_ShouldThrowException_WhenValidationFails() {
        doThrow(new IllegalArgumentException("Invalid training"))
                .when(entityValidator)
                .validateTrainingForCreation(SAMPLE_TRAINING);

        assertThrows(IllegalArgumentException.class, () -> service.createTraining(SAMPLE_TRAINING));
    }

    private static Training createTraining() {
        return Training.builder()
                .trainingId(TRAINING_ID)
                .traineeId(TRAINEE_ID)
                .trainerId(TRAINER_ID)
                .trainingName(TRAINING_NAME)
                .trainingDate(LocalDate.now())
                .trainingDuration(DURATION)
                .build();
    }
}