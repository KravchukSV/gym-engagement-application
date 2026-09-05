package com.gym.engagement.app.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class SharedStorageTest {

    @Test
    void shouldReturnSameStoragesThatWereInjected() {
        TrainerStorage trainerStorage = new TrainerStorage();
        TraineeStorage traineeStorage = new TraineeStorage();
        TrainingStorage trainingStorage = new TrainingStorage();

        SharedStorage sharedStorage = new SharedStorage(trainerStorage, traineeStorage, trainingStorage);

        assertSame(trainerStorage, sharedStorage.getTrainerStorage());
        assertSame(traineeStorage, sharedStorage.getTraineeStorage());
        assertSame(trainingStorage, sharedStorage.getTrainingStorage());
    }
}
