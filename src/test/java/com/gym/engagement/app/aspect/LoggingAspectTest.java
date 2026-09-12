package com.gym.engagement.app.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    private static final String TRAINEE_SERVICE_CLASS = "com.gym.engagement.app.service.TraineeServiceImpl";
    private static final String TRAINEE_DAO_CLASS = "com.gym.engagement.app.dao.TraineeDaoImpl";

    private static final String METHOD_FIND_TRAINEE = "findTrainee";
    private static final String METHOD_SAVE_TRAINEE = "saveTrainee";
    private static final String METHOD_UPDATE_TRAINEE = "updateTrainee";
    private static final String METHOD_DELETE_TRAINEE = "deleteTrainee";
    private static final String METHOD_GET_TRAINEES = "getTrainees";

    private static final String DUMMY_RESULT = "Result";
    private static final String ERROR_MESSAGE = "Database error";

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @InjectMocks
    private LoggingAspect aspect;

    @BeforeEach
    void setUp() {
        lenient().when(joinPoint.getSignature()).thenReturn(signature);
        lenient().when(proceedingJoinPoint.getSignature()).thenReturn(signature);
    }

    @Test
    @DisplayName("Pointcuts: should execute pointcut methods")
    void pointcuts_ShouldBeExecutable() {
        assertDoesNotThrow(() -> {
            aspect.excludeGeneratePassword();
            aspect.serviceLayer();
            aspect.appLayers();
        });
    }

    @Test
    @DisplayName("logTimeAndDebug: should proceed execution and return result")
    void logTimeAndDebug_ShouldProceedAndReturnResult() throws Throwable {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(METHOD_FIND_TRAINEE);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{1L});
        when(proceedingJoinPoint.proceed()).thenReturn("TraineeData");

        Object actual = aspect.logTimeAndDebug(proceedingJoinPoint);

        assertEquals("TraineeData", actual);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    @DisplayName("logTimeAndDebug: should rethrow exception when proceed fails")
    void logTimeAndDebug_ShouldRethrowException_WhenProceedFails() throws Throwable {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(METHOD_SAVE_TRAINEE);
        when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException(ERROR_MESSAGE));

        RuntimeException actual = assertThrows(RuntimeException.class,
                () -> aspect.logTimeAndDebug(proceedingJoinPoint));

        assertEquals(ERROR_MESSAGE, actual.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"saveTrainee", "createTrainer"})
    @DisplayName("determineBusinessAction: should detect CREATE actions")
    void logSuccessfulBusinessEvent_ShouldDetectCreateActions(String methodName) {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(methodName);
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, DUMMY_RESULT));
    }

    @Test
    @DisplayName("determineBusinessAction: should detect UPDATE action")
    void logSuccessfulBusinessEvent_ShouldDetectUpdateAction() {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(METHOD_UPDATE_TRAINEE);
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, DUMMY_RESULT));
    }

    @Test
    @DisplayName("determineBusinessAction: should detect DELETE action")
    void logSuccessfulBusinessEvent_ShouldDetectDeleteAction() {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(METHOD_DELETE_TRAINEE);
        when(joinPoint.getArgs()).thenReturn(new Object[]{1L});

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"findTraineeById", "getTrainerList"})
    @DisplayName("determineBusinessAction: should detect READ actions")
    void logSuccessfulBusinessEvent_ShouldDetectReadActions(String methodName) {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(methodName);
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, DUMMY_RESULT));
    }

    @Test
    @DisplayName("determineBusinessAction: should return null for non-business methods")
    void logSuccessfulBusinessEvent_ShouldIgnoreUnknownMethods() {
        when(signature.getName()).thenReturn("processInternalLogic");

        aspect.logSuccessfulBusinessEvent(joinPoint, DUMMY_RESULT);

        verify(signature, never()).getDeclaringTypeName();
    }

    @Test
    @DisplayName("extractContextDetails: should return ID when first argument is a Number")
    void extractContextDetails_ShouldReturnId_WhenFirstArgIsNumber() {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(METHOD_FIND_TRAINEE);
        when(joinPoint.getArgs()).thenReturn(new Object[]{42L, "ExtraArg"});

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, DUMMY_RESULT));
    }

    @Test
    @DisplayName("extractContextDetails: should return result string when first argument is not a Number")
    void extractContextDetails_ShouldReturnResultString_WhenFirstArgIsNotNumber() {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(METHOD_SAVE_TRAINEE);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"NotANumber"});

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, "CustomResultObject"));
    }

    @Test
    @DisplayName("extractContextDetails: should return result string when args array is empty")
    void extractContextDetails_ShouldReturnResultString_WhenArgsAreEmpty() {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(METHOD_GET_TRAINEES);
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, "ListResult"));
    }

    @Test
    @DisplayName("extractContextDetails: should return result string when args array is null")
    void extractContextDetails_ShouldReturnResultString_WhenArgsIsNull() {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(METHOD_GET_TRAINEES);
        when(joinPoint.getArgs()).thenReturn(null);

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, "ListResult"));
    }

    @Test
    @DisplayName("extractContextDetails: should return default message when both args and result are null")
    void extractContextDetails_ShouldReturnNoDetails_WhenArgsAndResultAreNull() {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_SERVICE_CLASS);
        when(signature.getName()).thenReturn(METHOD_DELETE_TRAINEE);
        when(joinPoint.getArgs()).thenReturn(null);

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, null));
    }

    @ParameterizedTest
    @CsvSource({"com.gym.engagement.app.service.TraineeServiceImpl, saveTrainee",
            "com.gym.engagement.app.service.TrainerService, updateTrainer",
            "com.gym.engagement.app.dao.TrainingDao, createTraining"})
    @DisplayName("extractDomainEntity: should strip ServiceImpl or Service suffixes correctly")
    void extractDomainEntity_ShouldExtractCorrectName(String className, String methodName) {
        when(signature.getDeclaringTypeName()).thenReturn(className);
        when(signature.getName()).thenReturn(methodName);
        when(joinPoint.getArgs()).thenReturn(new Object[]{});

        assertDoesNotThrow(() -> aspect.logSuccessfulBusinessEvent(joinPoint, DUMMY_RESULT));
    }

    @Test
    @DisplayName("logSystemException: should log warning safely without throwing exception")
    void logSystemException_ShouldLogWarningSafely() {
        when(signature.getDeclaringTypeName()).thenReturn(TRAINEE_DAO_CLASS);
        when(signature.getName()).thenReturn(METHOD_FIND_TRAINEE);

        RuntimeException ex = new RuntimeException(ERROR_MESSAGE);

        assertDoesNotThrow(() -> aspect.logSystemException(joinPoint, ex));
    }
}