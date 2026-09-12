package com.gym.engagement.app.mapper;

import com.gym.engagement.app.dto.TrainingTypeDto;
import com.gym.engagement.app.model.TrainingType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {

    TrainingType toEntity(TrainingTypeDto dto);

    TrainingTypeDto toDto(TrainingType entity);
}
