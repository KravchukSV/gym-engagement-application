package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.service.TraineeService;
import com.gym.engagement.app.service.common.CredentialsGenerator;
import com.gym.engagement.app.service.common.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

    private TraineeDao traineeDao;
    private CredentialsGenerator credentialsGenerator;
    private EntityValidator entityValidator;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setCredentialsGenerator(CredentialsGenerator credentialsGenerator) {
        this.credentialsGenerator = credentialsGenerator;
    }

    @Autowired
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Trainee saveTrainee(Trainee trainee) {
        entityValidator.validateUserForCreation(trainee);

        trainee = createTraineeWithCredentials(trainee);

        return traineeDao.save(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        entityValidator.validateUserForUpdate(trainee);

        return traineeDao.update(trainee.getUserId(), trainee)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Trainee with ID %d not found", trainee.getUserId())));
    }

    @Override
    public void deleteTrainee(Long id) {
        entityValidator.validateId(id);

        traineeDao.delete(id);
    }

    @Override
    public Optional<Trainee> findTraineeById(Long id) {
        entityValidator.validateId(id);

        return traineeDao.findById(id);
    }

    @Override
    public List<Trainee> findAllTrainees() {
        return traineeDao.findAll();
    }

    private Trainee createTraineeWithCredentials(Trainee trainee) {
        String username = credentialsGenerator.generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = credentialsGenerator.generatePassword();
        String encodedPassword = passwordEncoder.encode(password);

        return trainee.toBuilder()
                .username(username)
                .password(encodedPassword)
                .build();
    }
}
