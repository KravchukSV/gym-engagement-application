package com.gym.engagement.app.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User {

    @EqualsAndHashCode.Include
    private Long userId;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;

}
