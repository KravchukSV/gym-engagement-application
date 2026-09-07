package com.gym.engagement.app.storage;

import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.Trainee;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class StorageInitialData {

    private List<Trainee> trainees;
    private List<Trainer> trainers;
    private List<Training> trainings;
}
