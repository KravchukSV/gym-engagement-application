package com.gym.engagement.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    @Test
    @DisplayName("Should load application context and register all required beans")
    void shouldLoadApplicationContextFromAppConfig() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertNotNull(context.getBean(AppConfig.class));
            assertNotNull(context.getBean(ObjectMapper.class));
            assertNotNull(context.getBean(PasswordEncoder.class));
        }
    }
}