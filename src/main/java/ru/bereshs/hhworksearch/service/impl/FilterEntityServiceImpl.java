package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.FilterMapper;
import ru.bereshs.hhworksearch.repository.FilterEntityRepository;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.service.FilterEntityService;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FilterEntityServiceImpl implements FilterEntityService {
    private final FilterEntityRepository repository;
    private final FilterMapper mapper;

    public List<FilterEntity> getAll() {
        return repository.findAll();
    }


    public FilterEntity getById(Long id) throws HhWorkSearchException {
        if (id == null) {
            throw new HhWorkSearchException("Wrong parameter");
        }
        return repository.findById(id).orElseThrow(() -> new HhWorkSearchException("Wrong id"));
    }

    public String getKey() {
        return getScope(FilterScope.KEY).get(0).getWord();
    }


    public List<FilterEntity> getScope(FilterScope scope) {
        if (scope == null) {
            return new ArrayList<>();
        }
        return repository.findFilterEntityByScope(scope);
    }

    public void save(FilterEntity filterEntity) {
        if (filterEntity == null) {
            return;
        }
        repository.save(filterEntity);
    }


    public void update(Long id, FilterEntity filterEntity) throws HhWorkSearchException {
        if (id == null || filterEntity == null) {
            throw new HhWorkSearchException("Wrong parameters");
        }
        FilterEntity filterDb = getById(id);
        mapper.updateFilter(filterDb, filterEntity);

        save(filterDb);
    }

    public void delete(FilterEntity entity) {
        if (entity == null) {
            return;
        }

        repository.delete(entity);
    }
}
