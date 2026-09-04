package com.gym.engagement.app.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrainingType {

    @EqualsAndHashCode.Include
    private Long trainingTypeId;

    private String trainingTypeName;

}
