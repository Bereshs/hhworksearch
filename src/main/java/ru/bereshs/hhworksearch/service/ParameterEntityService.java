package ru.bereshs.hhworksearch.service;


import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.ParameterEntity;
import ru.bereshs.hhworksearch.model.ParameterType;

import java.util.List;
import java.util.Optional;

public interface ParameterEntityService {
    ParameterEntity getByType(ParameterType type) throws HhWorkSearchException;

    List<ParameterEntity> findAll();

    void save(ParameterEntity entity) throws HhWorkSearchException;

    void update(ParameterEntity entity) throws HhWorkSearchException;

    boolean isUnCompleted();
}
