package com.gym.engagement.app.util;

public interface CredentialsGenerator {
    String generateUsername(String firstName, String lastName);

    String generatePassword();
}