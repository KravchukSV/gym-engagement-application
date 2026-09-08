package com.gym.engagement.app.util.impl;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.util.CredentialsGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CredentialsGeneratorImpl implements CredentialsGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;

    private final SecureRandom random;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    @Override
    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        List<String> existingUsernames = getExistingUsernameVariants(baseUsername);

        if (existingUsernames.isEmpty()) {
            return baseUsername;
        }

        long maxSuffix = getMaxSuffix(existingUsernames, baseUsername);

        return baseUsername + (maxSuffix + 1);
    }

    @Override
    public String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }

    private List<String> getExistingUsernameVariants(String baseUsername) {
        List<Trainee> trainees = traineeDao.findAll();
        List<Trainer> trainers = trainerDao.findAll();

        return Stream.concat(trainees.stream().map(Trainee::getUsername),
                        trainers.stream().map(Trainer::getUsername))
                .filter(Objects::nonNull)
                .filter(username -> isValidUsernameVariant(username, baseUsername))
                .toList();
    }

    private boolean isValidUsernameVariant(String username, String baseUsername) {
        if (username.equals(baseUsername)) {
            return true;
        }

        if (!username.startsWith(baseUsername)) {
            return false;
        }

        String suffix = username.substring(baseUsername.length());

        return isNumeric(suffix);
    }

    private long getMaxSuffix(List<String> usernames, String baseUsername) {
        return usernames.stream()
                .filter(username -> username.length() > baseUsername.length())
                .map(username -> username.substring(baseUsername.length()))
                .filter(this::isNumeric)
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);
    }

    private boolean isNumeric(String value) {
        return !value.isEmpty() && value.chars().allMatch(Character::isDigit);
    }
}