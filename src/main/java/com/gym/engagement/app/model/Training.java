package com.gym.engagement.app.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Training {

    @EqualsAndHashCode.Include
    private Long trainingId;

    private final Long traineeId;
    private final Long trainerId;
    private final String trainingName;
    private final TrainingType trainingType;
    private final LocalDate trainingDate;
    private final Integer trainingDuration;

}