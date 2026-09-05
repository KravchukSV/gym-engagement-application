package com.gym.engagement.app.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrainingType {

    @EqualsAndHashCode.Include
    private final Long trainingTypeId;

    private final String trainingTypeName;

}