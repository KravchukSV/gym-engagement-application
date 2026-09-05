package com.gym.engagement.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User {

    @EqualsAndHashCode.Include
    private Long userId;

    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final Boolean isActive;

}