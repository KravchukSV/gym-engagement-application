package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TrainingDao;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.service.TrainingService;
import com.gym.engagement.app.service.common.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private TrainingDao trainingDao;
    private EntityValidator entityValidator;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Autowired
    public void setEntityValidator(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;
    }

    @Override
    public Training createTraining(Training training) {
        entityValidator.validateTrainingForCreation(training);

        return trainingDao.save(training);
    }

    @Override
    public Optional<Training> findTrainingById(Long id) {
        entityValidator.validateId(id);

        return trainingDao.findById(id);
    }

    @Override
    public List<Training> findAllTrainings() {
        return trainingDao.findAll();
    }
}
