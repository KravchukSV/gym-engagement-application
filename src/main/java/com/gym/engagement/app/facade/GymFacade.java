package com.gym.engagement.app.facade;

import com.gym.engagement.app.dto.TraineeDto;
import com.gym.engagement.app.dto.TrainerDto;
import com.gym.engagement.app.dto.TrainingDto;
import com.gym.engagement.app.mapper.TraineeMapper;
import com.gym.engagement.app.mapper.TrainerMapper;
import com.gym.engagement.app.mapper.TrainingMapper;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.service.TraineeService;
import com.gym.engagement.app.service.TrainerService;
import com.gym.engagement.app.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;

    public TraineeDto createTrainee(TraineeDto traineeDto) {
        Trainee trainee = traineeMapper.toEntity(traineeDto);
        Trainee savedTrainee = traineeService.saveTrainee(trainee);

        return traineeMapper.toDto(savedTrainee);
    }

    public TraineeDto updateTrainee(TraineeDto traineeDto) {
        Trainee trainee = traineeMapper.toEntity(traineeDto);
        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        return traineeMapper.toDto(updatedTrainee);
    }

    public void deleteTrainee(Long id) {
        traineeService.deleteTrainee(id);
    }

    public Optional<TraineeDto> findTraineeById(Long id) {
        Optional<Trainee> trainee = traineeService.findTraineeById(id);

        return trainee.map(traineeMapper::toDto);
    }

    public List<TraineeDto> findAllTrainees() {
        List<Trainee> trainees = traineeService.findAllTrainees();

        return trainees.stream()
                .map(traineeMapper::toDto)
                .toList();
    }

    public TrainerDto createTrainer(TrainerDto trainerDto) {
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        Trainer savedTrainer = trainerService.createTrainer(trainer);

        return trainerMapper.toDto(savedTrainer);
    }

    public TrainerDto updateTrainer(TrainerDto trainerDto) {
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        Trainer updatedTrainer = trainerService.updateTrainer(trainer);

        return trainerMapper.toDto(updatedTrainer);
    }

    public Optional<TrainerDto> findTrainerById(Long id) {
        Optional<Trainer> trainer = trainerService.findTrainerById(id);

        return trainer.map(trainerMapper::toDto);
    }

    public List<TrainerDto> findAllTrainers() {
        List<Trainer> trainers = trainerService.findAllTrainers();

        return trainers.stream()
                .map(trainerMapper::toDto)
                .toList();
    }

    public TrainingDto createTraining(TrainingDto trainingDto) {
        Training training = trainingMapper.toEntity(trainingDto);
        Training savedTraining = trainingService.createTraining(training);

        return trainingMapper.toDto(savedTraining);
    }

    public Optional<TrainingDto> findTrainingById(Long id) {
        Optional<Training> training = trainingService.findTrainingById(id);

        return training.map(trainingMapper::toDto);
    }

    public List<TrainingDto> findAllTrainings() {
        List<Training> trainings = trainingService.findAllTrainings();

        return trainings.stream()
                .map(trainingMapper::toDto)
                .toList();
    }
}