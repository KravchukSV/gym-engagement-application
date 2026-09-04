package com.gym.engagement.app.model;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Trainee extends User{

    private LocalDate dateOfBirth;
    private String address;

}
