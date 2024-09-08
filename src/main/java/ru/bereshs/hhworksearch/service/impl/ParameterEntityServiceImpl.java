package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.ParameterEntity;
import ru.bereshs.hhworksearch.model.ParameterType;
import ru.bereshs.hhworksearch.repository.ParameterEntityRepository;
import ru.bereshs.hhworksearch.service.ParameterEntityService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParameterEntityServiceImpl implements ParameterEntityService {
    private final ParameterEntityRepository repository;

    @Override
    public ParameterEntity getByType(ParameterType type) throws HhWorkSearchException {
        if (type == null) {
            throw new HhWorkSearchException("Wrong parameter type");
        }
        return repository.findByType(type).orElseThrow(() -> new HhWorkSearchException("Wrong parameter type"));
    }

    @Override
    public List<ParameterEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(ParameterEntity entity) throws HhWorkSearchException {
        if (entity == null) {
            throw new HhWorkSearchException("Wrong parameter entity");
        }
        repository.save(entity);
    }

    @Override
    public void update(ParameterEntity entity) throws HhWorkSearchException {
        if (entity == null || entity.getType() == null || entity.getData() == null) {
            throw new HhWorkSearchException("Wrong parameter entity");
        }

        ParameterEntity entityDb = getByType(entity.getType());

        entityDb.setData(entity.getData());
        save(entity);
    }

    @Override
    public boolean isUnCompleted() {
        try {
            ParameterEntity parameter = getByType(ParameterType.CLIENT_ID);
            List<ParameterEntity> list = findAll();

            return (list.size() < 4) || (parameter.getData() == null);

        } catch (HhWorkSearchException exception) {
            return true;
        }
    }
}
