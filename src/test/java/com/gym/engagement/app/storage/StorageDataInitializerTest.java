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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageDataInitializerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Resource dataFileResource;

    @Mock
    private InMemoryStorage storage;

    private final TraineeStorage traineeStorage = mock(TraineeStorage.class);
    private final TrainerStorage trainerStorage = mock(TrainerStorage.class);
    private final TrainingStorage trainingStorage = mock(TrainingStorage.class);

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

        Object actualBean = initializer.postProcessAfterInitialization(expectedBean, "someBean");

        assertSame(expectedBean, actualBean, "The method must return the exact same bean instance it received");
        verifyNoInteractions(dataFileResource, objectMapper);
    }

    @Test
    @DisplayName("Should exit early and return storage if data file resource does not exist")
    void shouldReturnStorageIfResourceDoesNotExist() {
        when(dataFileResource.exists()).thenReturn(false);
        Object expectedStorage = storage;

        Object actualStorage = initializer.postProcessAfterInitialization(storage, "inMemoryStorage");

        assertSame(expectedStorage, actualStorage, "The method must return the unmodified storage bean");
        verify(dataFileResource).exists();
        verifyNoMoreInteractions(dataFileResource);
        verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("Should successfully load Trainees from JSON into storage")
    void shouldLoadTraineesSuccessfully() throws IOException {
        Trainee expectedTrainee = Trainee.builder().userId(1L).build();
        StorageInitialData mockData = mock(StorageInitialData.class);
        when(mockData.getTrainees()).thenReturn(List.of(expectedTrainee));

        mockInitialDataLoad(mockData);
        when(storage.getTraineeStorage()).thenReturn(traineeStorage);

        ArgumentCaptor<Long> traineeIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Trainee> traineeCaptor = ArgumentCaptor.forClass(Trainee.class);

        Object actualStorage = initializer.postProcessAfterInitialization(storage, "inMemoryStorage");

        assertSame(storage, actualStorage, "The method must return the original storage bean instance");
        verify(traineeStorage).save(traineeIdCaptor.capture(), traineeCaptor.capture());
        assertEquals(1L, traineeIdCaptor.getValue(), "Actual Trainee ID does not match the expected value");
        assertEquals(expectedTrainee, traineeCaptor.getValue(), "Actual Trainee object does not match the expected value");
    }

    @Test
    @DisplayName("Should successfully load Trainers from JSON into storage")
    void shouldLoadTrainersSuccessfully() throws IOException {
        Trainer expectedTrainer = Trainer.builder().userId(2L).build();
        StorageInitialData mockData = mock(StorageInitialData.class);
        when(mockData.getTrainers()).thenReturn(List.of(expectedTrainer));

        mockInitialDataLoad(mockData);
        when(storage.getTrainerStorage()).thenReturn(trainerStorage);

        ArgumentCaptor<Long> trainerIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Trainer> trainerCaptor = ArgumentCaptor.forClass(Trainer.class);

        Object actualStorage = initializer.postProcessAfterInitialization(storage, "inMemoryStorage");

        assertSame(storage, actualStorage, "The method must return the original storage bean instance");
        verify(trainerStorage).save(trainerIdCaptor.capture(), trainerCaptor.capture());
        assertEquals(2L, trainerIdCaptor.getValue(), "Actual Trainer ID does not match the expected value");
        assertEquals(expectedTrainer, trainerCaptor.getValue(), "Actual Trainer object does not match the expected value");
    }

    @Test
    @DisplayName("Should successfully load Trainings from JSON into storage")
    void shouldLoadTrainingsSuccessfully() throws IOException {
        Training expectedTraining = Training.builder().trainingId(3L).build();
        StorageInitialData mockData = mock(StorageInitialData.class);
        when(mockData.getTrainings()).thenReturn(List.of(expectedTraining));

        mockInitialDataLoad(mockData);
        when(storage.getTrainingStorage()).thenReturn(trainingStorage);

        ArgumentCaptor<Long> trainingIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Training> trainingCaptor = ArgumentCaptor.forClass(Training.class);

        Object actualStorage = initializer.postProcessAfterInitialization(storage, "inMemoryStorage");

        assertSame(storage, actualStorage, "The method must return the original storage bean instance");
        verify(trainingStorage).save(trainingIdCaptor.capture(), trainingCaptor.capture());
        assertEquals(3L, trainingIdCaptor.getValue(), "Actual Training ID does not match the expected value");
        assertEquals(expectedTraining, trainingCaptor.getValue(), "Actual Training object does not match the expected value");
    }

    @Test
    @DisplayName("Should throw IllegalStateException when an IOException occurs during file read")
    void shouldThrowExceptionWhenIOExceptionOccurs() throws IOException {
        when(dataFileResource.exists()).thenReturn(true);
        String expectedMessagePart = "Failed to load initial data into storage";

        when(dataFileResource.getInputStream()).thenThrow(new IOException("File read error"));

        IllegalStateException actualException = assertThrows(IllegalStateException.class,
                () -> initializer.postProcessAfterInitialization(storage, "inMemoryStorage"));

        assertNotNull(actualException.getMessage(), "Exception message should not be null");
        assertTrue(actualException.getMessage().contains(expectedMessagePart),
                "Actual exception message should contain information about the loading failure");
    }


    private void mockInitialDataLoad(StorageInitialData mockData) throws IOException {
        when(dataFileResource.exists()).thenReturn(true);
        InputStream inputStream = new ByteArrayInputStream("{}".getBytes());
        when(dataFileResource.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(inputStream, StorageInitialData.class)).thenReturn(mockData);
    }

}