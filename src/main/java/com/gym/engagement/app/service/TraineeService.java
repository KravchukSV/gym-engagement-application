package com.gym.engagement.app.service;

import com.gym.engagement.app.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee saveTrainee(Trainee trainee);

    Trainee updateTrainee(Trainee trainee);

    void deleteTrainee(Long id);

    Optional<Trainee> findTraineeById(Long id);

    List<Trainee> findAllTrainees();
}