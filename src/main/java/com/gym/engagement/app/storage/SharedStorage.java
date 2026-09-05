package com.gym.engagement.app.storage;

import com.gym.engagement.app.model.EntityType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SharedStorage {
    private final Map<EntityType, AbstractStorage<?, ?>> storage;

    public SharedStorage(TrainerStorage trainerStorage,
                         TraineeStorage traineeStorage,
                         TrainingStorage trainingStorage) {
        this.storage = Map.of(
                EntityType.TRAINER, trainerStorage,
                EntityType.TRAINEE, traineeStorage,
                EntityType.TRAINING, trainingStorage
        );
    }

    public TrainerStorage getTrainerStorage() {
        return (TrainerStorage) storage.get(EntityType.TRAINER);
    }

    public TraineeStorage getTraineeStorage() {
        return (TraineeStorage) storage.get(EntityType.TRAINEE);
    }

    public TrainingStorage getTrainingStorage() {
        return (TrainingStorage) storage.get(EntityType.TRAINING);
    }
}