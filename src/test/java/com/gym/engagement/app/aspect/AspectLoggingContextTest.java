package com.gym.engagement.app.aspect;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.service.TraineeService;
import com.gym.engagement.app.service.common.CredentialsGenerator;
import com.gym.engagement.app.service.common.EntityValidator;
import com.gym.engagement.app.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AspectLoggingContextTest {

    private static final String FIRST_NAME = "Sam";
    private static final String LAST_NAME = "Serious";
    private static final String USERNAME = "SamSerious";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    private TraineeService proxyService;
    private ListAppender<ILoggingEvent> logAppender;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private CredentialsGenerator credentialsGenerator;

    @Mock
    private EntityValidator entityValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TraineeServiceImpl service;

    @BeforeEach
    void setUp() {
        AspectJProxyFactory factory = new AspectJProxyFactory(service);
        factory.addAspect(new LoggingAspect());
        proxyService = factory.getProxy();

        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        logger.setLevel(Level.INFO);

        logAppender = new ListAppender<>();
        logAppender.start();
        logger.addAppender(logAppender);
    }

    @Test
    @DisplayName("Should trigger LoggingAspect via AOP proxy and verify exact log message strings")
    void saveTrainee_shouldLogInfo_whenCreatingTrainee() {
        Trainee inputTrainee = Trainee.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();
        Trainee savedTrainee = inputTrainee.toBuilder()
                .userId(10L)
                .username(USERNAME)
                .build();

        when(credentialsGenerator.generateUsername(FIRST_NAME, LAST_NAME)).thenReturn(USERNAME);
        when(credentialsGenerator.generatePassword()).thenReturn(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(traineeDao.save(any(Trainee.class))).thenReturn(savedTrainee);

        proxyService.saveTrainee(inputTrainee);

        assertThat(logAppender.list)
                .hasSize(1)
                .allMatch(event -> event.getLevel() == Level.INFO)
                .extracting(ILoggingEvent::getFormattedMessage)
                .containsExactly(String.format("BUSINESS EVENT | Action: [CREATE] | Domain: [Trainee] | Context: [%s]", savedTrainee));
    }
}