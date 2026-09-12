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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    private static final Long TRAINEE_ID = 1L;
    private static final Long TRAINER_ID = 2L;
    private static final Long TRAINING_ID = 3L;
    private static final String TRAINEE_FIRST_NAME = "Tom";
    private static final String TRAINER_FIRST_NAME = "Alex";
    private static final String TRAINING_NAME = "Cardio";

    private static final Trainee TRAINEE = createTrainee();
    private static final TraineeDto TRAINEE_DTO = createTraineeDto();
    private static final Trainer TRAINER = createTrainer();
    private static final TrainerDto TRAINER_DTO = createTrainerDto();
    private static final Training TRAINING = createTraining();
    private static final TrainingDto TRAINING_DTO = createTrainingDto();

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private GymFacade facade;

    @Test
    @DisplayName("createTrainee should map DTO, save entity and return DTO")
    void createTrainee_ShouldMapSaveAndReturnDto() {
        when(traineeMapper.toEntity(TRAINEE_DTO)).thenReturn(TRAINEE);
        when(traineeService.saveTrainee(TRAINEE)).thenReturn(TRAINEE);
        when(traineeMapper.toDto(TRAINEE)).thenReturn(TRAINEE_DTO);

        TraineeDto actual = facade.createTrainee(TRAINEE_DTO);

        assertEquals(TRAINEE_DTO, actual);
        verify(traineeMapper).toEntity(TRAINEE_DTO);
        verify(traineeService).saveTrainee(TRAINEE);
        verify(traineeMapper).toDto(TRAINEE);
    }

    @Test
    @DisplayName("updateTrainee should map DTO, update entity and return DTO")
    void updateTrainee_ShouldMapUpdateAndReturnDto() {
        when(traineeMapper.toEntity(TRAINEE_DTO)).thenReturn(TRAINEE);
        when(traineeService.updateTrainee(TRAINEE)).thenReturn(TRAINEE);
        when(traineeMapper.toDto(TRAINEE)).thenReturn(TRAINEE_DTO);

        TraineeDto actual = facade.updateTrainee(TRAINEE_DTO);

        assertEquals(TRAINEE_DTO, actual);
        verify(traineeMapper).toEntity(TRAINEE_DTO);
        verify(traineeService).updateTrainee(TRAINEE);
        verify(traineeMapper).toDto(TRAINEE);
    }

    @Test
    @DisplayName("deleteTrainee should delegate to service")
    void deleteTrainee_ShouldDelegateToService() {
        facade.deleteTrainee(TRAINEE_ID);

        verify(traineeService).deleteTrainee(TRAINEE_ID);
        verifyNoInteractions(traineeMapper);
    }

    @Test
    @DisplayName("findTraineeById should return DTO when trainee exists")
    void findTraineeById_WhenExists_ShouldReturnDto() {
        when(traineeService.findTraineeById(TRAINEE_ID)).thenReturn(Optional.of(TRAINEE));
        when(traineeMapper.toDto(TRAINEE)).thenReturn(TRAINEE_DTO);

        Optional<TraineeDto> actual = facade.findTraineeById(TRAINEE_ID);

        assertTrue(actual.isPresent());
        assertEquals(TRAINEE_DTO, actual.get());
        verify(traineeService).findTraineeById(TRAINEE_ID);
        verify(traineeMapper).toDto(TRAINEE);
    }

    @Test
    @DisplayName("findTraineeById should return empty when trainee does not exist")
    void findTraineeById_WhenNotFound_ShouldReturnEmpty() {
        when(traineeService.findTraineeById(TRAINEE_ID)).thenReturn(Optional.empty());

        Optional<TraineeDto> actual = facade.findTraineeById(TRAINEE_ID);

        assertTrue(actual.isEmpty());
        verify(traineeService).findTraineeById(TRAINEE_ID);
        verifyNoInteractions(traineeMapper);
    }

    @Test
    @DisplayName("findAllTrainees should return mapped DTOs")
    void findAllTrainees_ShouldReturnMappedDtos() {
        when(traineeService.findAllTrainees()).thenReturn(List.of(TRAINEE));
        when(traineeMapper.toDto(TRAINEE)).thenReturn(TRAINEE_DTO);

        List<TraineeDto> actual = facade.findAllTrainees();

        assertEquals(List.of(TRAINEE_DTO), actual);
        verify(traineeService).findAllTrainees();
        verify(traineeMapper).toDto(TRAINEE);
    }

    @Test
    @DisplayName("createTrainer should map DTO, save entity and return DTO")
    void createTrainer_ShouldMapSaveAndReturnDto() {
        when(trainerMapper.toEntity(TRAINER_DTO)).thenReturn(TRAINER);
        when(trainerService.createTrainer(TRAINER)).thenReturn(TRAINER);
        when(trainerMapper.toDto(TRAINER)).thenReturn(TRAINER_DTO);

        TrainerDto actual = facade.createTrainer(TRAINER_DTO);

        assertEquals(TRAINER_DTO, actual);
        verify(trainerMapper).toEntity(TRAINER_DTO);
        verify(trainerService).createTrainer(TRAINER);
        verify(trainerMapper).toDto(TRAINER);
    }

    @Test
    @DisplayName("updateTrainer should map DTO, update entity and return DTO")
    void updateTrainer_ShouldMapUpdateAndReturnDto() {
        when(trainerMapper.toEntity(TRAINER_DTO)).thenReturn(TRAINER);
        when(trainerService.updateTrainer(TRAINER)).thenReturn(TRAINER);
        when(trainerMapper.toDto(TRAINER)).thenReturn(TRAINER_DTO);

        TrainerDto actual = facade.updateTrainer(TRAINER_DTO);

        assertEquals(TRAINER_DTO, actual);
        verify(trainerMapper).toEntity(TRAINER_DTO);
        verify(trainerService).updateTrainer(TRAINER);
        verify(trainerMapper).toDto(TRAINER);
    }

    @Test
    @DisplayName("findTrainerById should return DTO when trainer exists")
    void findTrainerById_WhenExists_ShouldReturnDto() {
        when(trainerService.findTrainerById(TRAINER_ID)).thenReturn(Optional.of(TRAINER));
        when(trainerMapper.toDto(TRAINER)).thenReturn(TRAINER_DTO);

        Optional<TrainerDto> actual = facade.findTrainerById(TRAINER_ID);

        assertTrue(actual.isPresent());
        assertEquals(TRAINER_DTO, actual.get());
        verify(trainerService).findTrainerById(TRAINER_ID);
        verify(trainerMapper).toDto(TRAINER);
    }

    @Test
    @DisplayName("findTrainerById should return empty when trainer does not exist")
    void findTrainerById_WhenNotFound_ShouldReturnEmpty() {
        when(trainerService.findTrainerById(TRAINER_ID)).thenReturn(Optional.empty());

        Optional<TrainerDto> actual = facade.findTrainerById(TRAINER_ID);

        assertTrue(actual.isEmpty());
        verify(trainerService).findTrainerById(TRAINER_ID);
        verifyNoInteractions(trainerMapper);
    }

    @Test
    @DisplayName("findAllTrainers should return mapped DTOs")
    void findAllTrainers_ShouldReturnMappedDtos() {
        when(trainerService.findAllTrainers()).thenReturn(List.of(TRAINER));
        when(trainerMapper.toDto(TRAINER)).thenReturn(TRAINER_DTO);

        List<TrainerDto> actual = facade.findAllTrainers();

        assertEquals(List.of(TRAINER_DTO), actual);
        verify(trainerService).findAllTrainers();
        verify(trainerMapper).toDto(TRAINER);
    }

    @Test
    @DisplayName("createTraining should map DTO, save entity and return DTO")
    void createTraining_ShouldMapSaveAndReturnDto() {
        when(trainingMapper.toEntity(TRAINING_DTO)).thenReturn(TRAINING);
        when(trainingService.createTraining(TRAINING)).thenReturn(TRAINING);
        when(trainingMapper.toDto(TRAINING)).thenReturn(TRAINING_DTO);

        TrainingDto actual = facade.createTraining(TRAINING_DTO);

        assertEquals(TRAINING_DTO, actual);
        verify(trainingMapper).toEntity(TRAINING_DTO);
        verify(trainingService).createTraining(TRAINING);
        verify(trainingMapper).toDto(TRAINING);
    }

    @Test
    @DisplayName("findTrainingById should return DTO when training exists")
    void findTrainingById_WhenExists_ShouldReturnDto() {
        when(trainingService.findTrainingById(TRAINING_ID)).thenReturn(Optional.of(TRAINING));
        when(trainingMapper.toDto(TRAINING)).thenReturn(TRAINING_DTO);

        Optional<TrainingDto> actual = facade.findTrainingById(TRAINING_ID);

        assertTrue(actual.isPresent());
        assertEquals(TRAINING_DTO, actual.get());
        verify(trainingService).findTrainingById(TRAINING_ID);
        verify(trainingMapper).toDto(TRAINING);
    }

    @Test
    @DisplayName("findTrainingById should return empty when training does not exist")
    void findTrainingById_WhenNotFound_ShouldReturnEmpty() {
        when(trainingService.findTrainingById(TRAINING_ID)).thenReturn(Optional.empty());

        Optional<TrainingDto> actual = facade.findTrainingById(TRAINING_ID);

        assertTrue(actual.isEmpty());
        verify(trainingService).findTrainingById(TRAINING_ID);
        verifyNoInteractions(trainingMapper);
    }

    @Test
    @DisplayName("findAllTrainings should return mapped DTOs")
    void findAllTrainings_ShouldReturnMappedDto() {
        when(trainingService.findAllTrainings()).thenReturn(List.of(TRAINING));
        when(trainingMapper.toDto(TRAINING)).thenReturn(TRAINING_DTO);

        List<TrainingDto> actual = facade.findAllTrainings();

        assertEquals(List.of(TRAINING_DTO), actual);
        verify(trainingService).findAllTrainings();
        verify(trainingMapper).toDto(TRAINING);
    }

    private static Trainee createTrainee() {
        return Trainee.builder()
                .userId(TRAINEE_ID)
                .firstName(TRAINEE_FIRST_NAME)
                .build();
    }

    private static TraineeDto createTraineeDto() {
        TraineeDto dto = new TraineeDto();
        dto.setUserId(TRAINEE_ID);
        dto.setFirstName(TRAINEE_FIRST_NAME);

        return dto;
    }

    private static Trainer createTrainer() {
        return Trainer.builder()
                .userId(TRAINER_ID)
                .firstName(TRAINER_FIRST_NAME)
                .build();
    }

    private static TrainerDto createTrainerDto() {
        TrainerDto dto = new TrainerDto();
        dto.setUserId(TRAINER_ID);
        dto.setFirstName(TRAINER_FIRST_NAME);

        return dto;
    }

    private static Training createTraining() {
        return Training.builder()
                .trainingId(TRAINING_ID)
                .trainingName(TRAINING_NAME)
                .build();
    }

    private static TrainingDto createTrainingDto() {
        TrainingDto dto = new TrainingDto();
        dto.setTrainingId(TRAINING_ID);
        dto.setTrainingName(TRAINING_NAME);

        return dto;
    }
}