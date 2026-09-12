package com.gym.engagement.app.mapper;

import com.gym.engagement.app.dto.TrainingDto;
import com.gym.engagement.app.dto.TrainingTypeDto;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.TrainingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class TrainingMapperTest {

    private static final Long TRAINING_ID = 10L;
    private static final Long TRAINEE_ID = 100L;
    private static final Long TRAINER_ID = 200L;
    private static final Long TRAINING_TYPE_ID = 1L;
    private static final String TRAINING_NAME = "Morning Cardio";
    private static final String TRAINING_TYPE_NAME = "Cardio";
    private static final LocalDate TRAINING_DATE = LocalDate.of(2026, 9, 15);
    private static final Integer TRAINING_DURATION = 60;

    @Spy
    private TrainingTypeMapper trainingTypeMapper = new TrainingTypeMapperImpl();

    @InjectMocks
    private TrainingMapperImpl mapper;

    @Test
    @DisplayName("Should map TrainingDto to Training entity")
    void shouldMapDtoToEntity() {
        TrainingTypeDto typeDto = new TrainingTypeDto();
        typeDto.setTrainingTypeId(TRAINING_TYPE_ID);
        typeDto.setTrainingTypeName(TRAINING_TYPE_NAME);
        TrainingDto dto = new TrainingDto();
        dto.setTrainingId(TRAINING_ID);
        dto.setTraineeId(TRAINEE_ID);
        dto.setTrainerId(TRAINER_ID);
        dto.setTrainingName(TRAINING_NAME);
        dto.setTrainingType(typeDto);
        dto.setTrainingDate(TRAINING_DATE);
        dto.setTrainingDuration(TRAINING_DURATION);

        Training actual = mapper.toEntity(dto);

        assertNotNull(actual);
        assertEquals(dto.getTrainingId(), actual.getTrainingId());
        assertEquals(dto.getTraineeId(), actual.getTraineeId());
        assertEquals(dto.getTrainerId(), actual.getTrainerId());
        assertEquals(dto.getTrainingName(), actual.getTrainingName());
        assertEquals(dto.getTrainingDate(), actual.getTrainingDate());
        assertEquals(dto.getTrainingDuration(), actual.getTrainingDuration());
        assertNotNull(actual.getTrainingType());
        assertEquals(typeDto.getTrainingTypeId(), actual.getTrainingType().getTrainingTypeId());
        assertEquals(typeDto.getTrainingTypeName(), actual.getTrainingType().getTrainingTypeName());
    }

    @Test
    @DisplayName("Should map Training entity to TrainingDto")
    void shouldMapEntityToDto() {
        TrainingType typeEntity = TrainingType.builder()
                .trainingTypeId(TRAINING_TYPE_ID)
                .trainingTypeName(TRAINING_TYPE_NAME)
                .build();
        Training entity = Training.builder()
                .trainingId(TRAINING_ID)
                .traineeId(TRAINEE_ID)
                .trainerId(TRAINER_ID)
                .trainingName(TRAINING_NAME)
                .trainingType(typeEntity)
                .trainingDate(TRAINING_DATE)
                .trainingDuration(TRAINING_DURATION)
                .build();

        TrainingDto actual = mapper.toDto(entity);

        assertNotNull(actual);
        assertEquals(entity.getTrainingId(), actual.getTrainingId());
        assertEquals(entity.getTraineeId(), actual.getTraineeId());
        assertEquals(entity.getTrainerId(), actual.getTrainerId());
        assertEquals(entity.getTrainingName(), actual.getTrainingName());
        assertEquals(entity.getTrainingDate(), actual.getTrainingDate());
        assertEquals(entity.getTrainingDuration(), actual.getTrainingDuration());
        assertNotNull(actual.getTrainingType());
        assertEquals(typeEntity.getTrainingTypeId(), actual.getTrainingType().getTrainingTypeId());
        assertEquals(typeEntity.getTrainingTypeName(), actual.getTrainingType().getTrainingTypeName());
    }

    @Test
    @DisplayName("Should return null when TrainingDto is null")
    void shouldReturnNullWhenDtoIsNull() {
        Training actual = mapper.toEntity(null);

        assertNull(actual);
    }

    @Test
    @DisplayName("Should return null when Training entity is null")
    void shouldReturnNullWhenEntityIsNull() {
        TrainingDto actual = mapper.toDto(null);

        assertNull(actual);
    }
}