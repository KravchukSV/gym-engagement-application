package com.gym.engagement.app.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageDataInitializer implements BeanPostProcessor {

    private final ObjectMapper objectMapper;

    @Value("classpath:${storage.data.file-path}")
    private Resource dataFileResource;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof InMemoryStorage storage) {
            log.info("Initializing in-memory storage");
            loadInitialData(storage);
            log.info("In-memory storage initialization completed");
        }

        return bean;
    }

    private void loadInitialData(InMemoryStorage storage) {
        if (dataFileResource == null || !dataFileResource.exists()) {
            log.warn("Storage initialization skipped: data file resource not found");

            return;
        }

        log.debug("Loading initial storage data from: {}", dataFileResource.getDescription());
        try (InputStream inputStream = dataFileResource.getInputStream()) {
            StorageInitialData data = objectMapper.readValue(inputStream, StorageInitialData.class);

            if (data == null) {
                log.warn("Storage initial data file is empty or contains null data");

                return;
            }

            saveAll(data.getTrainees(), Trainee::getUserId,
                    (id, item) -> storage.getTraineeStorage().save(id, item));
            saveAll(data.getTrainers(), Trainer::getUserId,
                    (id, item) -> storage.getTrainerStorage().save(id, item));
            saveAll(data.getTrainings(),
                    Training::getTrainingId, (id, item) -> storage.getTrainingStorage().save(id, item));

        } catch (IOException e) {
            log.error("Failed to load initial data into storage from {}", dataFileResource.getDescription(), e);
            throw new IllegalStateException("Failed to load initial data into storage from " + dataFileResource, e);
        }
    }

    private <K, V> void saveAll(Collection<V> entities, Function<V, K> idExtractor, BiConsumer<K, V> saver) {
        if (entities == null) {
            log.debug("No entities found for initialization");

            return;
        }

        entities.stream()
                .filter(Objects::nonNull)
                .filter(entity -> idExtractor.apply(entity) != null)
                .forEach(entity -> saver.accept(idExtractor.apply(entity), entity));
    }
}