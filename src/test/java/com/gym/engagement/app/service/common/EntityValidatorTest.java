package com.gym.engagement.app.service.common;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityValidatorTest {

    private static final Long VALID_ID = 1L;
    private static final String FIRST_NAME = "Serhii";
    private static final String LAST_NAME = "Kovalenko";
    private static final String TRAINING_NAME = "Powerlifting Workout";
    private static final int VALID_DURATION = 90;
    private static final int INVALID_DURATION = 0;

    private EntityValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EntityValidator();
    }

    @Test
    @DisplayName("validateUserForCreation() should pass for valid Trainee")
    void validateUserForCreation_ShouldPass_ForValidTrainee() {
        Trainee trainee = createTraineeBuilder().build();

        assertDoesNotThrow(() -> validator.validateUserForCreation(trainee));
    }

    @Test
    @DisplayName("validateUserForCreation() should pass for valid Trainer")
    void validateUserForCreation_ShouldPass_ForValidTrainer() {
        Trainer trainer = createTrainerBuilder().build();

        assertDoesNotThrow(() -> validator.validateUserForCreation(trainer));
    }

    @Test
    @DisplayName("validateUserForCreation() should throw when User is null")
    void validateUserForCreation_ShouldThrow_WhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateUserForCreation(null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    @DisplayName("validateUserForCreation() should throw when firstName is invalid")
    void validateUserForCreation_ShouldThrow_WhenFirstNameIsInvalid(String invalidName) {
        Trainee trainee = createTraineeBuilder()
                .firstName(invalidName)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validateUserForCreation(trainee));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    @DisplayName("validateUserForCreation() should throw when lastName is invalid")
    void validateUserForCreation_ShouldThrow_WhenLastNameIsInvalid(String invalidName) {
        Trainee trainee = createTraineeBuilder()
                .lastName(invalidName)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validateUserForCreation(trainee));
    }

    @Test
    @DisplayName("validateUserForCreation() should throw when Trainer specialization is null")
    void validateUserForCreation_ShouldThrow_WhenTrainerSpecializationIsNull() {
        Trainer trainer = createTrainerBuilder()
                .specialization(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validateUserForCreation(trainer));
    }

    @Test
    @DisplayName("validateUserForUpdate() should pass when User has valid ID and fields")
    void validateUserForUpdate_ShouldPass_WhenUserIsValid() {
        Trainee trainee = createTraineeBuilder()
                .userId(VALID_ID)
                .build();

        assertDoesNotThrow(() -> validator.validateUserForUpdate(trainee));
    }

    @Test
    @DisplayName("validateUserForUpdate() should throw when userId is null")
    void validateUserForUpdate_ShouldThrow_WhenUserIdIsNull() {
        Trainee trainee = createTraineeBuilder()
                .userId(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validateUserForUpdate(trainee));
    }

    @Test
    @DisplayName("validateTrainingForCreation() should pass for valid Training")
    void validateTrainingForCreation_ShouldPass_ForValidTraining() {
        Training training = createTrainingBuilder().build();

        assertDoesNotThrow(() -> validator.validateTrainingForCreation(training));
    }

    @Test
    @DisplayName("validateTrainingForCreation() should throw when Training is null")
    void validateTrainingForCreation_ShouldThrow_WhenTrainingIsNull() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateTrainingForCreation(null));
    }

    @Test
    @DisplayName("validateTrainingForCreation() should throw when traineeId is null")
    void validateTrainingForCreation_ShouldThrow_WhenTraineeIdIsNull() {
        Training training = createTrainingBuilder()
                .traineeId(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validateTrainingForCreation(training));
    }

    @Test
    @DisplayName("validateTrainingForCreation() should throw when trainerId is null")
    void validateTrainingForCreation_ShouldThrow_WhenTrainerIdIsNull() {
        Training training = createTrainingBuilder()
                .trainerId(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validateTrainingForCreation(training));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("validateTrainingForCreation() should throw when trainingName is invalid")
    void validateTrainingForCreation_ShouldThrow_WhenTrainingNameIsInvalid(String invalidName) {
        Training training = createTrainingBuilder()
                .trainingName(invalidName)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validateTrainingForCreation(training));
    }

    @Test
    @DisplayName("validateTrainingForCreation() should throw when trainingDate is null")
    void validateTrainingForCreation_ShouldThrow_WhenTrainingDateIsNull() {
        Training training = createTrainingBuilder()
                .trainingDate(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validateTrainingForCreation(training));
    }

    @Test
    @DisplayName("validateTrainingForCreation() should throw when duration is invalid")
    void validateTrainingForCreation_ShouldThrow_WhenDurationIsInvalid() {
        Training training = createTrainingBuilder()
                .trainingDuration(INVALID_DURATION)
                .build();

        assertThrows(IllegalArgumentException.class, () -> validator.validateTrainingForCreation(training));
    }

    @Test
    @DisplayName("validateId() should pass for valid ID")
    void validateId_ShouldPass_ForValidId() {
        assertDoesNotThrow(() -> validator.validateId(VALID_ID));
    }

    @Test
    @DisplayName("validateId() should throw when ID is null")
    void validateId_ShouldThrow_WhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateId(null));
    }

    private Trainee.TraineeBuilder<?, ?> createTraineeBuilder() {
        return Trainee.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME);
    }

    private Trainer.TrainerBuilder<?, ?> createTrainerBuilder() {
        return Trainer.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .specialization(
                        TrainingType.builder()
                                .trainingTypeName("Fitness")
                                .build()
                );
    }

    private Training.TrainingBuilder createTrainingBuilder() {
        return Training.builder()
                .traineeId(VALID_ID)
                .trainerId(VALID_ID)
                .trainingName(TRAINING_NAME)
                .trainingDate(LocalDate.now())
                .trainingDuration(VALID_DURATION);
    }
}