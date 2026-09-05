package com.gym.engagement.app.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractStorageTest {

    private final AbstractStorage<String, String> storage = new AbstractStorage<>() {
    };

    @Test
    void shouldSaveEntity() {
        String expected = "Alice";

        String actual = storage.save("trainer-1", expected);

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindSavedEntityById() {
        String expected = "Alice";
        storage.save("trainer-1", expected);

        Optional<String> actual = storage.findById("trainer-1");

        assertEquals(Optional.of(expected), actual);
    }

    @Test
    void shouldRejectNullIdOnSave() {
        Class<IllegalArgumentException> expected = IllegalArgumentException.class;

        Executable actual = () -> storage.save(null, "Alice");

        assertThrows(expected, actual);
    }

    @Test
    void shouldRejectNullEntityOnSave() {
        Class<IllegalArgumentException> expected = IllegalArgumentException.class;

        Executable actual = () -> storage.save("trainer-1", null);

        assertThrows(expected, actual);
    }

    @Test
    void shouldReturnEmptyOptionalForNullId() {
        Optional<String> expected = Optional.empty();

        Optional<String> actual = storage.findById(null);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnEmptyOptionalForMissingId() {
        Optional<String> expected = Optional.empty();

        Optional<String> actual = storage.findById("missing-id");

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnAllSavedEntities() {
        Set<String> expected = Set.of("Alice", "Bob");
        storage.save("trainer-1", "Alice");
        storage.save("trainer-2", "Bob");

        Set<String> actual = new HashSet<>(storage.findAll());

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteExistingEntity() {
        boolean expected = true;
        storage.save("trainer-1", "Alice");

        boolean actual = storage.delete("trainer-1");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotFindEntityAfterDelete() {
        boolean expected = false;
        storage.save("trainer-1", "Alice");
        storage.delete("trainer-1");

        boolean actual = storage.existsById("trainer-1");

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingEntity() {
        boolean expected = false;

        boolean actual = storage.delete("missing-id");

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnFalseWhenDeletingByNullId() {
        boolean expected = false;

        boolean actual = storage.delete(null);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnFalseWhenCheckingNullIdExistence() {
        boolean expected = false;

        boolean actual = storage.existsById(null);

        assertEquals(expected, actual);
    }
}
