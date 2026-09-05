package com.gym.engagement.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Trainee extends User{

    private final LocalDate dateOfBirth;
    private final String address;

}