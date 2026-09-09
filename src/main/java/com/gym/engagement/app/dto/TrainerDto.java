package com.gym.engagement.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TrainerDto extends UserDto {
    private TrainingTypeDto specialization;
}