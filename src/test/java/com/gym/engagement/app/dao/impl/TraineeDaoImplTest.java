package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.storage.InMemoryStorage;
import com.gym.engagement.app.storage.TraineeStorage;
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
class TraineeDaoImplTest {

    @Mock
    private InMemoryStorage inMemoryStorage;

    @Mock
    private TraineeStorage traineeStorage;

    private TraineeDaoImpl dao;

    @BeforeEach
    void setUp() {
        when(inMemoryStorage.getTraineeStorage()).thenReturn(traineeStorage);
        dao = new TraineeDaoImpl(inMemoryStorage);
    }

    @Test
    @DisplayName("save() should successfully store and return trainee")
    void save_ShouldReturnSavedTrainee() {
        Trainee expected = Trainee.builder().userId(1L).firstName("Serhii").lastName("Kovalenko").build();
        when(traineeStorage.save(1L, expected)).thenReturn(expected);

        Trainee actual = dao.save(expected);

        assertEquals(expected, actual);
        verify(traineeStorage, times(1)).save(1L, expected);
    }

    @Test
    @DisplayName("update() should call storage save and return Optional with updated trainee")
    void update_ShouldReturnOptionalWithUpdatedTrainee() {
        Trainee trainee = Trainee.builder().userId(1L).firstName("SerhiiUpdated").build();
        Optional<Trainee> expected = Optional.of(trainee);

        when(traineeStorage.save(1L, trainee)).thenReturn(trainee);

        Optional<Trainee> actual = dao.update(1L, trainee);

        assertEquals(expected, actual);
        verify(traineeStorage, times(1)).save(1L, trainee);
    }

    @Test
    @DisplayName("update() should return Optional.empty() when storage returns null")
    void update_ShouldReturnEmptyOptional_WhenStorageReturnsNull() {
        Trainee trainee = Trainee.builder().userId(1L).build();
        when(traineeStorage.save(1L, trainee)).thenReturn(null);

        Optional<Trainee> actual = dao.update(1L, trainee);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    @DisplayName("delete() should return true when trainee was deleted")
    void delete_ShouldReturnTrue_WhenDeleted() {
        boolean expected = true;
        when(traineeStorage.delete(1L)).thenReturn(expected);

        boolean actual = dao.delete(1L);

        assertEquals(expected, actual);
        verify(traineeStorage, times(1)).delete(1L);
    }

    @Test
    @DisplayName("findById() should return Optional with Trainee when exists")
    void findById_ShouldReturnTrainee_WhenExists() {
        Trainee trainee = Trainee.builder().userId(1L).build();
        Optional<Trainee> expected = Optional.of(trainee);

        when(traineeStorage.findById(1L)).thenReturn(expected);

        Optional<Trainee> actual = dao.findById(1L);

        assertEquals(expected, actual);
        verify(traineeStorage, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findAll() should return list of all trainees")
    void findAll_ShouldReturnTraineeList() {
        List<Trainee> expected = List.of(Trainee.builder().userId(1L).build());
        when(traineeStorage.findAll()).thenReturn(expected);

        List<Trainee> actual = dao.findAll();

        assertEquals(expected, actual);
        verify(traineeStorage, times(1)).findAll();
    }

    @Test
    @DisplayName("existsByUsername() should return false when username does not match")
    void existsByUsername_ShouldReturnFalse_WhenUsernameNotFound() {
        boolean expected = false;
        Trainee trainee = Trainee.builder().username("Serhii.Kovalenko").build();

        when(traineeStorage.findAll()).thenReturn(List.of(trainee));

        boolean actual = dao.existsByUsername("Alex.Smith");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("existsByUsername() should handle trainees with null username safely")
    void existsByUsername_ShouldHandleNullUsername() {
        boolean expected = false;
        Trainee traineeWithNullUsername = Trainee.builder().username(null).build();

        when(traineeStorage.findAll()).thenReturn(List.of(traineeWithNullUsername));

        boolean actual = dao.existsByUsername("serhii.kovalenko");

        assertEquals(expected, actual);
    }
}