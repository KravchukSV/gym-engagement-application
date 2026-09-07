package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.storage.InMemoryStorage;
import com.gym.engagement.app.storage.TrainerStorage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TrainerDaoImpl implements TrainerDao {

    private final TrainerStorage trainerStorage;

    public TrainerDaoImpl(InMemoryStorage inMemoryStorage) {
       this.trainerStorage = inMemoryStorage.getTrainerStorage();
   }

    @Override
    public Trainer save(Trainer trainer) {
        return trainerStorage.save(trainer.getUserId(), trainer);
    }

    @Override
    public Optional<Trainer> update(Long id, Trainer trainer) {
        return Optional.ofNullable(trainerStorage.save(id, trainer));
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return trainerStorage.findById(id);
    }

    @Override
    public List<Trainer> findAll() {
        return trainerStorage.findAll();
    }

    @Override
    public boolean existsByUsername(String username) {
        return trainerStorage.findAll().stream()
                .map(Trainer::getUsername)
                .filter(Objects::nonNull)
                .anyMatch(u -> u.equalsIgnoreCase(username));
    }
}
