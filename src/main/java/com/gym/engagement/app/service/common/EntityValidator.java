package com.gym.engagement.app.service.common;

import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.User;
import org.springframework.stereotype.Component;

@Component
public class EntityValidator {

    public void validateUserForCreation(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }

        validateString(user.getFirstName(), "First name");
        validateString(user.getLastName(), "Last name");

        if (user instanceof Trainer trainer) {
            if (trainer.getSpecialization() == null) {
                throw new IllegalArgumentException("Specialization cannot be null");
            }
        }
    }

    public void validateUserForUpdate(User user) {
        validateUserForCreation(user);
        validateId(user.getUserId());
    }

    public void validateTrainingForCreation(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Training object cannot be null");
        }

        if (training.getTraineeId() == null) {
            throw new IllegalArgumentException("Trainee ID cannot be null");
        }

        if (training.getTrainerId() == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }

        validateString(training.getTrainingName(), "Training name");
        if (training.getTrainingDate() == null) {
            throw new IllegalArgumentException("Training date cannot be null");
        }

        if (training.getTrainingDuration() == null || training.getTrainingDuration() <= 0) {
            throw new IllegalArgumentException("Training duration must be positive");
        }
    }

    public void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
    }

    private void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}