package com.gym.engagement.app.mapper;

import com.gym.engagement.app.dto.TrainerDto;
import com.gym.engagement.app.model.Trainer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TrainingTypeMapper.class})
public interface TrainerMapper {
    Trainer toEntity(TrainerDto dto);

    TrainerDto toDto(Trainer entity);
}
