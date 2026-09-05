package com.gym.engagement.app.storage;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractStorageTest {

    private final AbstractStorage<String, String> storage = new AbstractStorage<>() {
    };

    @Test
    void shouldSaveEntityAndReturnIt() {
        String saved = storage.save("trainer-1", "Alice");

        assertEquals("Alice", saved);
        assertEquals("Alice", storage.findById("trainer-1").orElseThrow());
    }

    @Test
    void shouldRejectNullIdOrEntityOnSave() {
        assertThrows(IllegalArgumentException.class, () -> storage.save(null, "Alice"));
        assertThrows(IllegalArgumentException.class, () -> storage.save("trainer-1", null));
    }

    @Test
    void shouldReturnEmptyOptionalWhenIdIsNullOrMissing() {
        assertTrue(storage.findById(null).isEmpty());
        assertTrue(storage.findById("missing-id").isEmpty());
    }

    @Test
    void shouldReturnAllSavedEntities() {
        storage.save("trainer-1", "Alice");
        storage.save("trainer-2", "Bob");

        Set<String> actualValues = new HashSet<>(storage.findAll());

        assertEquals(Set.of("Alice", "Bob"), actualValues);
    }

    @Test
    void shouldDeleteEntityAndCheckExistence() {
        storage.save("trainer-1", "Alice");

        assertTrue(storage.existsById("trainer-1"));
        assertTrue(storage.delete("trainer-1"));
        assertFalse(storage.existsById("trainer-1"));
        assertFalse(storage.delete("trainer-1"));
    }

    @Test
    void shouldReturnFalseForNullIdInDeleteAndExistsCheck() {
        assertFalse(storage.delete(null));
        assertFalse(storage.existsById(null));
    }
}
