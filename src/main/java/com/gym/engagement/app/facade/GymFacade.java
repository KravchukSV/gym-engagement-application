package com.gym.engagement.app.facade;

import com.gym.engagement.app.dto.TraineeDto;
import com.gym.engagement.app.dto.TrainerDto;
import com.gym.engagement.app.dto.TrainingDto;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.service.TraineeService;
import com.gym.engagement.app.service.TrainerService;
import com.gym.engagement.app.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final ModelMapper modelMapper;

    public TraineeDto createTrainee(TraineeDto traineeDto) {
        Trainee trainee = modelMapper.map(traineeDto, Trainee.class);
        Trainee savedTrainee = traineeService.saveTrainee(trainee);

        return modelMapper.map(savedTrainee, TraineeDto.class);
    }

    public TraineeDto updateTrainee(TraineeDto traineeDto) {
        Trainee trainee = modelMapper.map(traineeDto, Trainee.class);
        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        return modelMapper.map(updatedTrainee, TraineeDto.class);
    }

    public void deleteTrainee(Long id) {
        traineeService.deleteTrainee(id);
    }

    public Optional<TraineeDto> findTraineeById(Long id) {
        Optional<Trainee> trainee = traineeService.findTraineeById(id);

        return trainee.map(t -> modelMapper.map(t, TraineeDto.class));
    }

    public List<TraineeDto> findAllTrainees() {
        List<Trainee> trainees = traineeService.findAllTrainees();

        return trainees.stream()
                .map(trainee -> modelMapper.map(trainee, TraineeDto.class))
                .toList();
    }

    public TrainerDto createTrainer(TrainerDto trainerDto) {
        Trainer trainer = modelMapper.map(trainerDto, Trainer.class);
        Trainer savedTrainer = trainerService.createTrainer(trainer);

        return modelMapper.map(savedTrainer, TrainerDto.class);
    }

    public TrainerDto updateTrainer(TrainerDto trainerDto) {
        Trainer trainer = modelMapper.map(trainerDto, Trainer.class);
        Trainer updatedTrainer = trainerService.updateTrainer(trainer);

        return modelMapper.map(updatedTrainer, TrainerDto.class);
    }

    public Optional<TrainerDto> findTrainerById(Long id) {
        Optional<Trainer> trainer = trainerService.findTrainerById(id);

        return trainer.map(t -> modelMapper.map(t, TrainerDto.class));
    }

    public List<TrainerDto> findAllTrainers() {
        List<Trainer> trainers = trainerService.findAllTrainers();

        return trainers.stream()
                .map(trainer -> modelMapper.map(trainer, TrainerDto.class))
                .toList();
    }

    public TrainingDto createTraining(TrainingDto trainingDto) {
        Training training = modelMapper.map(trainingDto, Training.class);
        Training savedTraining = trainingService.createTraining(training);

        return modelMapper.map(savedTraining, TrainingDto.class);
    }

    public Optional<TrainingDto> findTrainingById(Long id) {
        Optional<Training> training = trainingService.findTrainingById(id);

        return training.map(t -> modelMapper.map(t, TrainingDto.class));
    }

    public List<TrainingDto> findAllTrainings() {
        List<Training> trainings = trainingService.findAllTrainings();

        return trainings.stream()
                .map(training -> modelMapper.map(training, TrainingDto.class))
                .toList();
    }
}