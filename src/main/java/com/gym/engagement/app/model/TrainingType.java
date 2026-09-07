package com.gym.engagement.app.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Jacksonized
public class TrainingType {

    @EqualsAndHashCode.Include
    private final Long trainingTypeId;

    private final String trainingTypeName;

}