package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.storage.InMemoryStorage;
import com.gym.engagement.app.storage.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingDaoImplTest {

    @Mock
    private InMemoryStorage inMemoryStorage;

    @Mock
    private TrainingStorage trainingStorage;

    private TrainingDaoImpl trainingDao;

    @BeforeEach
    void setUp() {
        when(inMemoryStorage.getTrainingStorage()).thenReturn(trainingStorage);
        trainingDao = new TrainingDaoImpl(inMemoryStorage);
    }

    @Test
    @DisplayName("save() should pass training and its ID to TrainingStorage")
    void save_ShouldSaveTraining() {
        Training expected = Training.builder().trainingId(10L).trainingName("Cardio").build();
        when(trainingStorage.save(10L, expected)).thenReturn(expected);

        Training actual = trainingDao.save(expected);

        assertEquals(expected, actual);
        verify(trainingStorage, times(1)).save(10L, expected);
    }

    @Test
    @DisplayName("findById() should return Optional with Training when found")
    void findById_ShouldReturnTraining() {
        Training training = Training.builder().trainingId(10L).build();
        Optional<Training> expected = Optional.of(training);
        when(trainingStorage.findById(10L)).thenReturn(expected);

        Optional<Training> actual = trainingDao.findById(10L);

        assertEquals(expected, actual);
        verify(trainingStorage, times(1)).findById(10L);
    }

    @Test
    @DisplayName("findAll() should return list of all trainings")
    void findAll_ShouldReturnTrainings() {
        List<Training> expected = List.of(Training.builder().trainingName("Powerlifting").build());
        when(trainingStorage.findAll()).thenReturn(expected);

        List<Training> actual = trainingDao.findAll();

        assertEquals(expected, actual);
        verify(trainingStorage, times(1)).findAll();
    }
}