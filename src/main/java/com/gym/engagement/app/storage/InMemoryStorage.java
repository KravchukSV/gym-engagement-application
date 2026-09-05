package com.gym.engagement.app.storage;

import com.gym.engagement.app.model.EntityType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.gym.engagement.app.model.EntityType.TRAINEE;
import static com.gym.engagement.app.model.EntityType.TRAINER;
import static com.gym.engagement.app.model.EntityType.TRAINING;

@Component
public class InMemoryStorage {
    private final Map<EntityType, AbstractStorage<?, ?>> storage;

    public InMemoryStorage(TrainerStorage trainerStorage,
                           TraineeStorage traineeStorage,
                           TrainingStorage trainingStorage) {
        this.storage = Map.of(TRAINER, trainerStorage,
                TRAINEE, traineeStorage,
                TRAINING, trainingStorage);
    }

    public TrainerStorage getTrainerStorage() {
        return (TrainerStorage) storage.get(TRAINER);
    }

    public TraineeStorage getTraineeStorage() {
        return (TraineeStorage) storage.get(TRAINEE);
    }

    public TrainingStorage getTrainingStorage() {
        return (TrainingStorage) storage.get(TRAINING);
    }
}