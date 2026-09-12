package com.gym.engagement.app.mapper;

import com.gym.engagement.app.dto.TraineeDto;
import com.gym.engagement.app.model.Trainee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    Trainee toEntity(TraineeDto dto);

    TraineeDto toDto(Trainee entity);
}
