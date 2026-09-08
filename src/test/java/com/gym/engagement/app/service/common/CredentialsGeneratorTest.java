package com.gym.engagement.app.service.common;

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
class CredentialsGeneratorTest {

    private static final String FIRST_NAME = "Serhii";
    private static final String LAST_NAME = "Kovalenko";
    private static final String BASE_USERNAME = FIRST_NAME + "." + LAST_NAME;
    private static final String USERNAME_SUFFIX_1 = BASE_USERNAME + "1";
    private static final String USERNAME_SUFFIX_2 = BASE_USERNAME + "2";

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private SecureRandom random;

    @InjectMocks
    private CredentialsGenerator generator;

    @Test
    @DisplayName("generateUsername() should return base username when no matching variants exist")
    void generateUsername_ShouldReturnBaseUsername_WhenNoMatches() {
        when(traineeDao.findAll()).thenReturn(List.of());
        when(trainerDao.findAll()).thenReturn(List.of());

        String actual = generator.generateUsername(FIRST_NAME, LAST_NAME);

        assertEquals(BASE_USERNAME, actual);
    }

    @Test
    @DisplayName("generateUsername() should return baseUsername + 1 when only base username exists")
    void generateUsername_ShouldAppendOne_WhenOnlyBaseUsernameExists() {
        Trainee trainee = createTrainee(BASE_USERNAME);

        when(traineeDao.findAll()).thenReturn(List.of(trainee));
        when(trainerDao.findAll()).thenReturn(List.of());

        String actual = generator.generateUsername(FIRST_NAME, LAST_NAME);

        assertEquals(USERNAME_SUFFIX_1, actual);
    }

    @Test
    @DisplayName("generateUsername() should increment max suffix when numbered variants exist")
    void generateUsername_ShouldIncrementMaxSuffix_WhenNumberedVariantsExist() {
        Trainee trainee1 = createTrainee(BASE_USERNAME);
        Trainee trainee2 = createTrainee(USERNAME_SUFFIX_1);
        Trainer trainer = createTrainer("Andrii.Melnyk");

        when(traineeDao.findAll()).thenReturn(List.of(trainee1, trainee2));
        when(trainerDao.findAll()).thenReturn(List.of(trainer));

        String actual = generator.generateUsername(FIRST_NAME, LAST_NAME);

        assertEquals(USERNAME_SUFFIX_2, actual);
    }

    @Test
    @DisplayName("generateUsername() should ignore non-numeric suffixes or unrelated prefixes")
    void generateUsername_ShouldIgnoreInvalidVariants() {
        Trainee trainee1 = createTrainee(BASE_USERNAME + "ABC");
        Trainee trainee2 = createTrainee("Other.User");
        Trainee trainee3 = createTrainee(null);

        when(traineeDao.findAll()).thenReturn(List.of(trainee1, trainee2, trainee3));
        when(trainerDao.findAll()).thenReturn(List.of());

        String actual = generator.generateUsername(FIRST_NAME, LAST_NAME);

        assertEquals(BASE_USERNAME, actual);
    }

    @Test
    @DisplayName("generatePassword() should return random 10-character password")
    void generatePassword_ShouldReturnValidPassword() {
        when(random.nextInt(62)).thenReturn(0);

        String actual = generator.generatePassword();

        assertNotNull(actual);
        assertEquals(10, actual.length());
        assertEquals("AAAAAAAAAA", actual);
    }

    private Trainee createTrainee(String username) {
        return Trainee.builder()
                .username(username)
                .build();
    }

    private Trainer createTrainer(String username) {
        return Trainer.builder()
                .username(username)
                .build();
    }
}