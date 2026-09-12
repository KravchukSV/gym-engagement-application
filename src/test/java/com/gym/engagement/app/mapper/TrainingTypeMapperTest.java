package com.gym.engagement.app.mapper;

import com.gym.engagement.app.dto.TrainingTypeDto;
import com.gym.engagement.app.model.TrainingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TrainingTypeMapperTest {

    private static final Long TRAINING_TYPE_ID = 1L;
    private static final String TRAINING_TYPE_NAME = "Fitness";

    private final TrainingTypeMapper mapper = Mappers.getMapper(TrainingTypeMapper.class);

    @Test
    @DisplayName("Should map TrainingTypeDto to TrainingType entity")
    void shouldMapDtoToEntity() {
        TrainingTypeDto dto = new TrainingTypeDto();
        dto.setTrainingTypeId(TRAINING_TYPE_ID);
        dto.setTrainingTypeName(TRAINING_TYPE_NAME);

        TrainingType actual = mapper.toEntity(dto);

        assertNotNull(actual);
        assertEquals(dto.getTrainingTypeId(), actual.getTrainingTypeId());
        assertEquals(dto.getTrainingTypeName(), actual.getTrainingTypeName());
    }

    @Test
    @DisplayName("Should map TrainingType entity to TrainingTypeDto")
    void shouldMapEntityToDto() {
        TrainingType entity = TrainingType.builder()
                .trainingTypeId(TRAINING_TYPE_ID)
                .trainingTypeName(TRAINING_TYPE_NAME)
                .build();

        TrainingTypeDto actual = mapper.toDto(entity);

        assertNotNull(actual);
        assertEquals(entity.getTrainingTypeId(), actual.getTrainingTypeId());
        assertEquals(entity.getTrainingTypeName(), actual.getTrainingTypeName());
    }

    @Test
    @DisplayName("Should return null when input is null")
    void shouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toDto(null));
    }
}