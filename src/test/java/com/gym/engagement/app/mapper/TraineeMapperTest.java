package com.gym.engagement.app.mapper;

import com.gym.engagement.app.dto.TraineeDto;
import com.gym.engagement.app.model.Trainee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TraineeMapperTest {

    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "Sam";
    private static final String LAST_NAME = "Serious";
    private static final String USERNAME = "Sam.Serious";
    private static final String PASSWORD = "pass";
    private static final Boolean IS_ACTIVE = true;
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1995, 1, 1);
    private static final String ADDRESS = "Funny St";

    private final TraineeMapper mapper = Mappers.getMapper(TraineeMapper.class);

    @Test
    @DisplayName("Should map TraineeDto to Trainee entity")
    void shouldMapDtoToEntity() {
        TraineeDto dto = new TraineeDto();
        dto.setUserId(USER_ID);
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setUsername(USERNAME);
        dto.setPassword(PASSWORD);
        dto.setIsActive(IS_ACTIVE);
        dto.setDateOfBirth(DATE_OF_BIRTH);
        dto.setAddress(ADDRESS);

        Trainee actual = mapper.toEntity(dto);

        assertNotNull(actual);
        assertEquals(dto.getUserId(), actual.getUserId());
        assertEquals(dto.getFirstName(), actual.getFirstName());
        assertEquals(dto.getLastName(), actual.getLastName());
        assertEquals(dto.getUsername(), actual.getUsername());
        assertEquals(dto.getPassword(), actual.getPassword());
        assertEquals(dto.getIsActive(), actual.getIsActive());
        assertEquals(dto.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(dto.getAddress(), actual.getAddress());
    }

    @Test
    @DisplayName("Should map Trainee entity to TraineeDto")
    void shouldMapEntityToDto() {
        Trainee entity = Trainee.builder()
                .userId(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .username(USERNAME)
                .password(PASSWORD)
                .isActive(IS_ACTIVE)
                .dateOfBirth(DATE_OF_BIRTH)
                .address(ADDRESS)
                .build();

        TraineeDto actual = mapper.toDto(entity);

        assertNotNull(actual);
        assertEquals(entity.getUserId(), actual.getUserId());
        assertEquals(entity.getFirstName(), actual.getFirstName());
        assertEquals(entity.getLastName(), actual.getLastName());
        assertEquals(entity.getUsername(), actual.getUsername());
        assertEquals(entity.getPassword(), actual.getPassword());
        assertEquals(entity.getIsActive(), actual.getIsActive());
        assertEquals(entity.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(entity.getAddress(), actual.getAddress());
    }

    @Test
    @DisplayName("Should return null when TraineeDto is null")
    void shouldReturnNullWhenDtoIsNull() {
        Trainee actual = mapper.toEntity(null);

        assertNull(actual);
    }

    @Test
    @DisplayName("Should return null when Trainee entity is null")
    void shouldReturnNullWhenEntityIsNull() {
        TraineeDto actual = mapper.toDto(null);

        assertNull(actual);
    }
}