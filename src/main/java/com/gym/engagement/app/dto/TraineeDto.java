package com.gym.engagement.app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TraineeDto extends UserDto {
    private LocalDate dateOfBirth;
    private String address;
}