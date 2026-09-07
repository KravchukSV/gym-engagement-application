package com.gym.engagement.app.dao;

import com.gym.engagement.app.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    Trainee save(Trainee trainee);

    Optional<Trainee> update(Long id, Trainee trainee);

    boolean delete(Long id);

    Optional<Trainee> findById(Long id);

    List<Trainee> findAll();

    boolean existsByUsername(String username);
}