package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.storage.InMemoryStorage;
import com.gym.engagement.app.storage.TraineeStorage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TraineeDaoImpl implements TraineeDao {

    private final TraineeStorage traineeStorage;

    public TraineeDaoImpl(InMemoryStorage inMemoryStorage) {
        this.traineeStorage = inMemoryStorage.getTraineeStorage();
    }

    @Override
    public Trainee save(Trainee trainee) {
        return traineeStorage.save(trainee.getUserId(), trainee);
    }

    @Override
    public Optional<Trainee> update(Long id, Trainee trainee) {
        if (findById(id).isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(traineeStorage.save(id, trainee));
    }

    @Override
    public boolean delete(Long id) {
        return traineeStorage.delete(id);
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return traineeStorage.findById(id);
    }

    @Override
    public List<Trainee> findAll() {
        return traineeStorage.findAll();
    }

    @Override
    public boolean existsByUsername(String username) {
        return traineeStorage.findAll().stream()
                .map(Trainee::getUsername)
                .filter(Objects::nonNull)
                .anyMatch(u -> u.equalsIgnoreCase(username));
    }
}
