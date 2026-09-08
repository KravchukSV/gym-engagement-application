package com.gym.engagement.app.service;

import com.gym.engagement.app.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingService {
    Training createTraining(Training training);

    Optional<Training> findTrainingById(Long id);

    List<Training> findAllTrainings();
}