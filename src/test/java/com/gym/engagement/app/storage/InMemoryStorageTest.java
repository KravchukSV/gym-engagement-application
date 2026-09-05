package com.gym.engagement.app.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class InMemoryStorageTest {

    private final TrainerStorage trainerStorage = new TrainerStorage();
    private final TraineeStorage traineeStorage = new TraineeStorage();
    private final TrainingStorage trainingStorage = new TrainingStorage();

    private final InMemoryStorage sharedStorage = new InMemoryStorage(trainerStorage,
            traineeStorage,
            trainingStorage);

    @Test
    void shouldReturnInjectedTrainerStorage() {
        TrainerStorage actual = sharedStorage.getTrainerStorage();

        assertSame(trainerStorage, actual);
    }

    @Test
    void shouldReturnInjectedTraineeStorage() {
        TraineeStorage actual = sharedStorage.getTraineeStorage();

        assertSame(traineeStorage, actual);
    }

    @Test
    void shouldReturnInjectedTrainingStorage() {
        TrainingStorage actual = sharedStorage.getTrainingStorage();

        assertSame(trainingStorage, actual);
    }
}
