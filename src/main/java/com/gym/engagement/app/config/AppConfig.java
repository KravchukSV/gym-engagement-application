package com.gym.engagement.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.gym.engagement.app"})
@PropertySource(value = "classpath:application.properties")
public class AppConfig {
}
