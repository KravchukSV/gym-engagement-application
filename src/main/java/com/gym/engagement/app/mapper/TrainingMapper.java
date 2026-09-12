package com.gym.engagement.app.mapper;

import com.gym.engagement.app.dto.TrainingDto;
import com.gym.engagement.app.model.Training;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TrainingTypeMapper.class})
public interface TrainingMapper {

    Training toEntity(TrainingDto dto);

    TrainingDto toDto(Training entity);
}
