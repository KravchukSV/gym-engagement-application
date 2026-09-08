package com.gym.engagement.app.service;

import com.gym.engagement.app.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(Trainer trainer);

    Trainer updateTrainer(Trainer trainer);

    Optional<Trainer> findTrainerById(Long id);

    List<Trainer> findAllTrainers();
}