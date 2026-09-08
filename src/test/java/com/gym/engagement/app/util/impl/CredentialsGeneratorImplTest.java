package com.gym.engagement.app.util.impl;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CredentialsGeneratorImplTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private SecureRandom random;

    @InjectMocks
    private CredentialsGeneratorImpl credentialsGenerator;

    @Test
    @DisplayName("generateUsername() should return base username when no matching variants exist")
    void generateUsername_ShouldReturnBaseUsername_WhenNoMatches() {
        String expected = "Serhii.Kovalenko";

        when(traineeDao.findAll()).thenReturn(List.of());
        when(trainerDao.findAll()).thenReturn(List.of());

        String actual = credentialsGenerator.generateUsername("Serhii", "Kovalenko");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("generateUsername() should return baseUsername + 1 when only base username exists")
    void generateUsername_ShouldAppendOne_WhenOnlyBaseUsernameExists() {
        String expected = "Serhii.Kovalenko1";
        Trainee trainee = Trainee.builder().username("Serhii.Kovalenko").build();

        when(traineeDao.findAll()).thenReturn(List.of(trainee));
        when(trainerDao.findAll()).thenReturn(List.of());

        String actual = credentialsGenerator.generateUsername("Serhii", "Kovalenko");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("generateUsername() should increment max suffix when numbered variants exist")
    void generateUsername_ShouldIncrementMaxSuffix_WhenNumberedVariantsExist() {
        String expected = "Serhii.Kovalenko2";
        Trainee trainee1 = Trainee.builder().username("Serhii.Kovalenko").build();
        Trainee trainee2 = Trainee.builder().username("Serhii.Kovalenko1").build();
        Trainer trainer = Trainer.builder().username("Andrii.Melnyk").build();

        when(traineeDao.findAll()).thenReturn(List.of(trainee1, trainee2));
        when(trainerDao.findAll()).thenReturn(List.of(trainer));

        String actual = credentialsGenerator.generateUsername("Serhii", "Kovalenko");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("generateUsername() should ignore non-numeric suffixes or unrelated prefixes")
    void generateUsername_ShouldIgnoreInvalidVariants() {
        String expected = "Serhii.Kovalenko";
        Trainee trainee1 = Trainee.builder().username("Serhii.KovalenkoABC").build();
        Trainee trainee2 = Trainee.builder().username("Other.User").build();
        Trainee trainee3 = Trainee.builder().username(null).build();

        when(traineeDao.findAll()).thenReturn(List.of(trainee1, trainee2, trainee3));
        when(trainerDao.findAll()).thenReturn(List.of());

        String actual = credentialsGenerator.generateUsername("Serhii", "Kovalenko");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("generatePassword() should return random 10-character password")
    void generatePassword_ShouldReturnValidPassword() {
        when(random.nextInt(62)).thenReturn(0);

        String actual = credentialsGenerator.generatePassword();

        assertNotNull(actual);
        assertEquals(10, actual.length());
        assertEquals("AAAAAAAAAA", actual);
    }
}