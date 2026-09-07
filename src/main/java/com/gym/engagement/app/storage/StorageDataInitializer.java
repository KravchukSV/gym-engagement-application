package com.gym.engagement.app.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class StorageDataInitializer implements BeanPostProcessor {

    @Value("classpath:${storage.data.file-path}")
    private Resource dataFileResource;

    private final ObjectMapper objectMapper;

    public StorageDataInitializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof InMemoryStorage storage) {
            loadInitialData(storage);
        }

        return bean;
    }

    private void loadInitialData(InMemoryStorage storage) {
        if (dataFileResource == null || !dataFileResource.exists()) {
            return;
        }

        try (InputStream inputStream = dataFileResource.getInputStream()) {
            StorageInitialData data = objectMapper.readValue(inputStream, StorageInitialData.class);

            if (data == null) {
                return;
            }

            if (data.getTrainees() != null) {
                for (Trainee trainee : data.getTrainees()) {
                    if (trainee != null && trainee.getUserId() != null) {
                        storage.getTraineeStorage().save(trainee.getUserId(), trainee);
                    }
                }
            }

            if (data.getTrainers() != null) {
                for (Trainer trainer : data.getTrainers()) {
                    if (trainer != null && trainer.getUserId() != null) {
                        storage.getTrainerStorage().save(trainer.getUserId(), trainer);
                    }
                }
            }

            if (data.getTrainings() != null) {
                for (Training training : data.getTrainings()) {
                    if (training != null && training.getTrainingId() != null) {
                        storage.getTrainingStorage().save(training.getTrainingId(), training);
                    }
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("Failed to load initial data into storage from " + dataFileResource, e);
        }
    }
}