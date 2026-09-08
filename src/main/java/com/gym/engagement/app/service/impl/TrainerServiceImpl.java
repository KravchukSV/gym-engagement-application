package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.service.TrainerService;
import com.gym.engagement.app.service.common.CredentialsGenerator;
import com.gym.engagement.app.service.common.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private TrainerDao trainerDao;
    private CredentialsGenerator credentialsGenerator;
    private EntityValidator entityValidator;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setCredentialsGenerator(CredentialsGenerator credentialsGenerator) {
        this.credentialsGenerator = credentialsGenerator;
    }

    @Autowired
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        entityValidator.validateUserForCreation(trainer);

        trainer = createTrainerWithCredentials(trainer);

        return trainerDao.save(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        entityValidator.validateUserForUpdate(trainer);

        return trainerDao.update(trainer.getUserId(), trainer)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Trainer with ID %d not found", trainer.getUserId())));
    }

    @Override
    public Optional<Trainer> findTrainerById(Long id) {
        entityValidator.validateId(id);

        return trainerDao.findById(id);
    }

    @Override
    public List<Trainer> findAllTrainers() {
        return trainerDao.findAll();
    }

    private Trainer createTrainerWithCredentials(Trainer trainer) {
        String username = credentialsGenerator.generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = credentialsGenerator.generatePassword();

        return trainer.toBuilder()
                .username(username)
                .password(password)
                .build();
    }
}
