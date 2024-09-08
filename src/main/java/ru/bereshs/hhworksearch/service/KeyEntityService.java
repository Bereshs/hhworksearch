package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.model.KeyEntity;

public interface KeyEntityService {

    KeyEntity getByUserId(Long userId);
    boolean validateKey(KeyEntity key);
    boolean isExpired(KeyEntity key);
    void save(KeyEntity key);
}
