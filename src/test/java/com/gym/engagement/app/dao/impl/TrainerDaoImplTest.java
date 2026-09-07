package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.storage.InMemoryStorage;
import com.gym.engagement.app.storage.TrainerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerDaoImplTest {

    @Mock
    private InMemoryStorage inMemoryStorage;

    @Mock
    private TrainerStorage trainerStorage;

    private TrainerDaoImpl trainerDao;

    @BeforeEach
    void setUp() {
        when(inMemoryStorage.getTrainerStorage()).thenReturn(trainerStorage);
        trainerDao = new TrainerDaoImpl(inMemoryStorage);
    }

    @Test
    @DisplayName("save() should delegate saving to TrainerStorage")
    void save_ShouldReturnSavedTrainer() {
        Trainer expected = Trainer.builder().userId(2L).firstName("Alex").build();
        when(trainerStorage.save(2L, expected)).thenReturn(expected);

        Trainer actual = trainerDao.save(expected);

        assertEquals(expected, actual);
        verify(trainerStorage).save(2L, expected);
    }

    @Test
    @DisplayName("update() should save updated trainer and return Optional")
    void update_ShouldReturnOptionalWithUpdatedTrainer() {
        Trainer trainer = Trainer.builder().userId(2L).firstName("AlexUpdated").build();
        Optional<Trainer> expected = Optional.of(trainer);
        when(trainerStorage.save(2L, trainer)).thenReturn(trainer);

        Optional<Trainer> actual = trainerDao.update(2L, trainer);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("update() should return Optional.empty() when storage returns null")
    void update_ShouldReturnEmptyOptional_WhenNull() {
        Trainer trainer = Trainer.builder().userId(2L).build();
        when(trainerStorage.save(2L, trainer)).thenReturn(null);

        Optional<Trainer> actual = trainerDao.update(2L, trainer);

        assertEquals(Optional.empty(), actual);
    }

    @Test
    @DisplayName("findById() should return Optional with Trainer when found")
    void findById_ShouldReturnTrainer() {
        Trainer trainer = Trainer.builder().userId(2L).build();
        Optional<Trainer> expected = Optional.of(trainer);
        when(trainerStorage.findById(2L)).thenReturn(expected);

        Optional<Trainer> actual = trainerDao.findById(2L);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findAll() should return list of all trainers")
    void findAll_ShouldReturnTrainerList() {
        List<Trainer> expected = List.of(Trainer.builder().userId(2L).build());
        when(trainerStorage.findAll()).thenReturn(expected);

        List<Trainer> actual = trainerDao.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("existsByUsername() should return true when username matches")
    void existsByUsername_ShouldReturnTrue_WhenExists() {
        Boolean expected = true;
        Trainer trainer = Trainer.builder().username("Alex.Smith").build();
        when(trainerStorage.findAll()).thenReturn(List.of(trainer));

        boolean actual = trainerDao.existsByUsername("alex.smith");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("existsByUsername() should return false when username does not exist")
    void existsByUsername_ShouldReturnFalse_WhenNotFound() {
        Boolean expected = false;
        when(trainerStorage.findAll()).thenReturn(List.of());

        boolean actual = trainerDao.existsByUsername("NonExistent.User");

        assertEquals(expected, actual);
    }
}
