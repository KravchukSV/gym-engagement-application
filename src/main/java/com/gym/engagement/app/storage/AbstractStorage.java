package com.gym.engagement.app.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractStorage<ID, T> implements Storage<ID, T> {
    private final Map<ID, T> storageMap = new ConcurrentHashMap<>();

    @Override
    public T save(ID id, T entity) {
        if (id == null || entity == null) {
            throw new IllegalArgumentException("ID and entity cannot be null");
        }
        storageMap.put(id, entity);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storageMap.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storageMap.values());
    }

    @Override
    public boolean delete(ID id) {
        if (id == null) {
            return false;
        }
        return storageMap.remove(id) != null;
    }

    @Override
    public boolean existsById(ID id) {
        if (id == null) {
            return false;
        }
        return storageMap.containsKey(id);
    }
}
