package com.gym.engagement.app.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageDataInitializerTest {

    @Captor
    private ArgumentCaptor<Long> IdCaptor;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Resource dataFileResource;

    @Mock
    private InMemoryStorage storage;

    @Mock
    private TraineeStorage traineeStorage;

    @Mock
    private TrainerStorage trainerStorage;

    @Mock
    private TrainingStorage trainingStorage;

    @Mock
    private StorageInitialData mockData;

    @InjectMocks
    private StorageDataInitializer initializer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(initializer, "dataFileResource", dataFileResource);
    }

    @Test
    @DisplayName("Should ignore bean if it is not an instance of InMemoryStorage")
    void shouldReturnSameBeanIfNotInMemoryStorage() {
        Object expectedBean = new Object();

        Object actual = initializer.postProcessAfterInitialization(expectedBean, "someBean");

        assertSame(expectedBean, actual, "The method must return the exact same bean instance it received");
        verifyNoInteractions(dataFileResource, objectMapper);
    }

    @Test
    @DisplayName("Should exit early and return storage if data file resource does not exist")
    void shouldReturnStorageIfResourceDoesNotExist() {
        when(dataFileResource.exists()).thenReturn(false);
        Object expectedStorage = storage;

        Object actual = initializer.postProcessAfterInitialization(storage, "inMemoryStorage");

        assertSame(expectedStorage, actual, "The method must return the unmodified storage bean");
        verify(dataFileResource).exists();
        verifyNoMoreInteractions(dataFileResource);
        verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("Should successfully load Trainees from JSON into storage")
    void shouldLoadTraineesSuccessfully() throws IOException {
        Trainee expectedTrainee = Trainee.builder().userId(1L).build();

        mockInitialDataLoad();
        when(mockData.getTrainees()).thenReturn(List.of(expectedTrainee));
        when(storage.getTraineeStorage()).thenReturn(traineeStorage);

        ArgumentCaptor<Trainee> traineeCaptor = ArgumentCaptor.forClass(Trainee.class);

        Object actual = initializer.postProcessAfterInitialization(storage, "inMemoryStorage");

        assertSame(storage, actual, "The method must return the original storage bean instance");
        verify(traineeStorage).save(IdCaptor.capture(), traineeCaptor.capture());
        assertEquals(1L, IdCaptor.getValue(), "Actual Trainee ID does not match the expected value");
        assertEquals(expectedTrainee, traineeCaptor.getValue(), "Actual Trainee object does not match the expected value");
    }

    @Test
    @DisplayName("Should successfully load Trainers from JSON into storage")
    void shouldLoadTrainersSuccessfully() throws IOException {
        Trainer expectedTrainer = Trainer.builder().userId(2L).build();

        mockInitialDataLoad();
        when(mockData.getTrainers()).thenReturn(List.of(expectedTrainer));
        when(storage.getTrainerStorage()).thenReturn(trainerStorage);

        ArgumentCaptor<Trainer> trainerCaptor = ArgumentCaptor.forClass(Trainer.class);

        Object actual = initializer.postProcessAfterInitialization(storage, "inMemoryStorage");

        assertSame(storage, actual, "The method must return the original storage bean instance");
        verify(trainerStorage).save(IdCaptor.capture(), trainerCaptor.capture());
        assertEquals(2L, IdCaptor.getValue(), "Actual Trainer ID does not match the expected value");
        assertEquals(expectedTrainer, trainerCaptor.getValue(), "Actual Trainer object does not match the expected value");
    }

    @Test
    @DisplayName("Should successfully load Trainings from JSON into storage")
    void shouldLoadTrainingsSuccessfully() throws IOException {
        Training expectedTraining = Training.builder().trainingId(3L).build();
        when(mockData.getTrainings()).thenReturn(List.of(expectedTraining));

        mockInitialDataLoad();
        when(storage.getTrainingStorage()).thenReturn(trainingStorage);

        ArgumentCaptor<Training> trainingCaptor = ArgumentCaptor.forClass(Training.class);

        Object actual = initializer.postProcessAfterInitialization(storage, "inMemoryStorage");

        assertSame(storage, actual, "The method must return the original storage bean instance");
        verify(trainingStorage).save(IdCaptor.capture(), trainingCaptor.capture());
        assertEquals(3L, IdCaptor.getValue(), "Actual Training ID does not match the expected value");
        assertEquals(expectedTraining, trainingCaptor.getValue(), "Actual Training object does not match the expected value");
    }

    @Test
    @DisplayName("Should throw IllegalStateException when an IOException occurs during file read")
    void shouldThrowExceptionWhenIOExceptionOccurs() throws IOException {
        String expectedMessagePart = "Failed to load initial data into storage";

        when(dataFileResource.exists()).thenReturn(true);
        when(dataFileResource.getInputStream()).thenThrow(new IOException("File read error"));

        IllegalStateException actual = assertThrows(IllegalStateException.class,
                () -> initializer.postProcessAfterInitialization(storage, "inMemoryStorage"));

        assertNotNull(actual.getMessage(), "Exception message should not be null");
        assertTrue(actual.getMessage().contains(expectedMessagePart),
                "Actual exception message should contain information about the loading failure");
    }

    private void mockInitialDataLoad() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("{}".getBytes());

        when(dataFileResource.exists()).thenReturn(true);
        when(dataFileResource.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(inputStream, StorageInitialData.class)).thenReturn(mockData);
    }
}