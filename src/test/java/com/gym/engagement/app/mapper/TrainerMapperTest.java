package com.gym.engagement.app.mapper;

import com.gym.engagement.app.dto.TrainerDto;
import com.gym.engagement.app.dto.TrainingTypeDto;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.TrainingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class TrainerMapperTest {

    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "Sam";
    private static final String LAST_NAME = "Serious";
    private static final String USERNAME = "Sam.Serious";
    private static final String PASSWORD = "pass";
    private static final Boolean IS_ACTIVE = true;
    private static final Long SPECIALIZATION_ID = 2L;
    private static final String SPECIALIZATION_NAME = "Fitness";

    @Spy
    private final TrainingTypeMapper trainingTypeMapper = new TrainingTypeMapperImpl();

    @InjectMocks
    private TrainerMapperImpl mapper;

    @Test
    @DisplayName("Should map TrainerDto to Trainer entity with specialization")
    void shouldMapDtoToEntity() {
        TrainingTypeDto specDto = new TrainingTypeDto();
        specDto.setTrainingTypeId(SPECIALIZATION_ID);
        specDto.setTrainingTypeName(SPECIALIZATION_NAME);
        TrainerDto dto = new TrainerDto();
        dto.setUserId(USER_ID);
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);
        dto.setIsActive(IS_ACTIVE);
        dto.setSpecialization(specDto);

        Trainer actual = mapper.toEntity(dto);

        assertNotNull(actual);
        assertEquals(dto.getUserId(), actual.getUserId());
        assertEquals(dto.getFirstName(), actual.getFirstName());
        assertEquals(dto.getLastName(), actual.getLastName());
        assertEquals(dto.getUsername(), actual.getUsername());
        assertEquals(dto.getPassword(), actual.getPassword());
        assertEquals(dto.getIsActive(), actual.getIsActive());
        assertNotNull(actual.getSpecialization());
        assertEquals(specDto.getTrainingTypeId(), actual.getSpecialization().getTrainingTypeId());
        assertEquals(specDto.getTrainingTypeName(), actual.getSpecialization().getTrainingTypeName());
    }

    @Test
    @DisplayName("Should map Trainer entity to TrainerDto with specialization")
    void shouldMapEntityToDto() {
        TrainingType specEntity = TrainingType.builder()
                .trainingTypeId(SPECIALIZATION_ID)
                .trainingTypeName(SPECIALIZATION_NAME)
                .build();
        Trainer entity = Trainer.builder()
                .userId(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .username(USERNAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .specialization(specEntity)
                .build();

        TrainerDto actual = mapper.toDto(entity);

        assertNotNull(actual);
        assertEquals(entity.getUserId(), actual.getUserId());
        assertEquals(entity.getFirstName(), actual.getFirstName());
        assertEquals(entity.getLastName(), actual.getLastName());
        assertEquals(entity.getUsername(), actual.getUsername());
        assertEquals(entity.getPassword(), actual.getPassword());
        assertEquals(entity.getIsActive(), actual.getIsActive());
        assertNotNull(actual.getSpecialization());
        assertEquals(specEntity.getTrainingTypeId(), actual.getSpecialization().getTrainingTypeId());
        assertEquals(specEntity.getTrainingTypeName(), actual.getSpecialization().getTrainingTypeName());
    }

    @Test
    @DisplayName("Should return null when TrainerDto is null")
    void shouldReturnNullWhenDtoIsNull() {
        Trainer actual = mapper.toEntity(null);

        assertNull(actual);
    }

    @Test
    @DisplayName("Should return null when Trainer entity is null")
    void shouldReturnNullWhenEntityIsNull() {
        TrainerDto actual = mapper.toDto(null);

        assertNull(actual);
    }
}