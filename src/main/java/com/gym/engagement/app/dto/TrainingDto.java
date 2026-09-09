package com.gym.engagement.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TrainingDto {
    private Long trainingId;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private TrainingTypeDto trainingType;
    private LocalDate trainingDate;
    private Integer trainingDuration;
}