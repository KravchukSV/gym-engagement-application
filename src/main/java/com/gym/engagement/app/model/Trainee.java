package com.gym.engagement.app.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@ToString(callSuper = true)
public class Trainee extends User{

    private final LocalDate dateOfBirth;
    private final String address;

}