package com.gym.engagement.app.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<ID, T> {
    T save(ID id, T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean delete(ID id);

    boolean existsById(ID id);
}
