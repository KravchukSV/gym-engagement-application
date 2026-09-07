package com.gym.engagement.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@ComponentScan(basePackages = {"com.gym.engagement.app"})
@PropertySource(value = "classpath:application.properties")
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}
