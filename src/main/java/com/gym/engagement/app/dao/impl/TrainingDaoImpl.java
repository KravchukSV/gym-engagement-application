package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.dao.TrainingDao;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.storage.InMemoryStorage;
import com.gym.engagement.app.storage.TrainingStorage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDaoImpl implements TrainingDao {

    private final TrainingStorage trainingStorage;

    public TrainingDaoImpl(InMemoryStorage inMemoryStorage) {
        this.trainingStorage = inMemoryStorage.getTrainingStorage();
    }

    @Override
    public Training save(Training training) {
        return trainingStorage.save(training.getTrainingId(), training);

    }

    @Override
    public Optional<Training> findById(Long id) {
        return trainingStorage.findById(id);
    }

    @Override
    public List<Training> findAll() {
        return trainingStorage.findAll();
    }
}
