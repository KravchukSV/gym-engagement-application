package com.gym.engagement.app.dao;

import com.gym.engagement.app.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingDao {
    Training save(Training training);

    Optional<Training> findById(Long id);

    List<Training> findAll();
}