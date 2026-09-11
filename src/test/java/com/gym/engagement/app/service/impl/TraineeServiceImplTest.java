package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.exception.EntityNotFoundException;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.service.common.CredentialsGenerator;
import com.gym.engagement.app.service.common.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    private static final Long TRAINEE_ID = 1L;
    private static final String FIRST_NAME = "Serhii";
    private static final String LAST_NAME = "Kovalenko";
    private static final String GENERATED_USERNAME = FIRST_NAME + "." + LAST_NAME;
    private static final String GENERATED_PASSWORD = "Password123";
    private static final String ENCODED_PASSWORD = "$2a$10$e8R4a1H1zP4uL4J3o4I0e.3n2m1k0j9i8h7g6f5e4d3c2b1a";
    private static final String ADDRESS = "Kyiv, Ukraine";

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private CredentialsGenerator credentialsGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityValidator entityValidator;

    @InjectMocks
    private TraineeServiceImpl service;

    private Trainee sampleTrainee;

    @BeforeEach
    void setUp() {
        sampleTrainee = createTraineeBuilder().build();
    }

    @Test
    @DisplayName("saveTrainee() should validate, generate credentials, and save trainee")
    void saveTrainee_ShouldGenerateCredentialsAndSave() {
        when(credentialsGenerator.generateUsername(FIRST_NAME, LAST_NAME)).thenReturn(GENERATED_USERNAME);
        when(credentialsGenerator.generatePassword()).thenReturn(GENERATED_PASSWORD);
        when(passwordEncoder.encode(GENERATED_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(traineeDao.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainee actual = service.saveTrainee(sampleTrainee);

        verify(entityValidator).validateUserForCreation(sampleTrainee);
        verify(credentialsGenerator).generateUsername(FIRST_NAME, LAST_NAME);
        verify(credentialsGenerator).generatePassword();
        verify(passwordEncoder).encode(GENERATED_PASSWORD);
        verify(traineeDao).save(any(Trainee.class));
        assertEquals(GENERATED_USERNAME, actual.getUsername());
        assertEquals(ENCODED_PASSWORD, actual.getPassword());
        assertEquals(FIRST_NAME, actual.getFirstName());
        assertEquals(LAST_NAME, actual.getLastName());
        assertEquals(ADDRESS, actual.getAddress());
    }

    @Test
    @DisplayName("updateTrainee() should validate and update trainee when found")
    void updateTrainee_ShouldUpdate_WhenFound() {
        Trainee traineeToUpdate = createTraineeBuilder()
                .userId(TRAINEE_ID)
                .build();

        when(traineeDao.update(TRAINEE_ID, traineeToUpdate)).thenReturn(Optional.of(traineeToUpdate));

        Trainee actual = service.updateTrainee(traineeToUpdate);

        verify(entityValidator).validateUserForUpdate(traineeToUpdate);
        verify(traineeDao).update(TRAINEE_ID, traineeToUpdate);
        assertEquals(traineeToUpdate, actual);
    }

    @Test
    @DisplayName("updateTrainee() should throw exception when trainee is not found")
    void updateTrainee_ShouldThrowException_WhenNotFound() {
        Trainee traineeToUpdate = createTraineeBuilder()
                .userId(TRAINEE_ID)
                .build();

        when(traineeDao.update(TRAINEE_ID, traineeToUpdate)).thenReturn(Optional.empty());

        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> service.updateTrainee(traineeToUpdate));

        verify(entityValidator).validateUserForUpdate(traineeToUpdate);
        verify(traineeDao).update(TRAINEE_ID, traineeToUpdate);
        assertEquals(String.format("Trainee with ID %d not found", TRAINEE_ID), actual.getMessage());
    }

    @Test
    @DisplayName("deleteTrainee() should validate ID and call DAO delete")
    void deleteTrainee_ShouldValidateAndDelegateToDao() {
        service.deleteTrainee(TRAINEE_ID);

        verify(entityValidator).validateId(TRAINEE_ID);
        verify(traineeDao).delete(TRAINEE_ID);
    }

    @Test
    @DisplayName("findTraineeById() should validate ID and return Optional Trainee")
    void findTraineeById_ShouldReturnTrainee() {
        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.of(sampleTrainee));

        Optional<Trainee> actual = service.findTraineeById(TRAINEE_ID);

        verify(entityValidator).validateId(TRAINEE_ID);
        verify(traineeDao).findById(TRAINEE_ID);
        assertTrue(actual.isPresent());
        assertEquals(sampleTrainee, actual.get());
    }

    @Test
    @DisplayName("findAllTrainees() should return list of trainees")
    void findAllTrainees_ShouldReturnList() {
        List<Trainee> expected = List.of(sampleTrainee);
        when(traineeDao.findAll()).thenReturn(expected);

        List<Trainee> actual = service.findAllTrainees();

        verify(traineeDao).findAll();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("saveTrainee() should throw exception when validation fails")
    void saveTrainee_ShouldThrowException_WhenValidationFails() {
        doThrow(new IllegalArgumentException("Invalid user"))
                .when(entityValidator)
                .validateUserForCreation(sampleTrainee);

        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> service.saveTrainee(sampleTrainee));

        assertEquals("Invalid user", actual.getMessage());
        verify(entityValidator).validateUserForCreation(sampleTrainee);
        verifyNoInteractions(credentialsGenerator, traineeDao);
    }

    private Trainee.TraineeBuilder<?, ?> createTraineeBuilder() {
        return Trainee.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .dateOfBirth(LocalDate.of(2000, 1, 1));
    }
}