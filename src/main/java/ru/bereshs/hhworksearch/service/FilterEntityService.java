package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.FilterEntity;
import ru.bereshs.hhworksearch.model.FilterScope;

import java.util.List;

public interface FilterEntityService {
    List<FilterEntity> getAll();
    FilterEntity getById(Long id) throws HhWorkSearchException;
    String getKey();
    List<FilterEntity> getScope(FilterScope scope);
    void save(FilterEntity filterEntity);
    void update(Long id, FilterEntity filterEntity) throws HhWorkSearchException;
    void delete(FilterEntity entity);


}
