package com.gym.engagement.app.dao;

import com.gym.engagement.app.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    Trainer save(Trainer trainer);

    Optional<Trainer> update(Long id, Trainer trainer);

    Optional<Trainer> findById(Long id);

    List<Trainer> findAll();

    boolean existsByUsername(String username);
}