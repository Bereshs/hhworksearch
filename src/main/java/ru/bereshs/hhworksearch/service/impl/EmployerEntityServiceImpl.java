package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.mapper.EmployerMapper;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;
import ru.bereshs.hhworksearch.repository.EmployerEntityRepository;
import ru.bereshs.hhworksearch.service.EmployerEntityService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerEntityServiceImpl implements EmployerEntityService {

    private final EmployerEntityRepository repository;
    private final EmployerMapper mapper;


    public EmployerEntity getByHhId(String hhId) {
        return repository.getByHhId(hhId);
    }


    public void saveAllIfNotExist(List<EmployerEntity> list) {
        list.forEach(e -> {
            if (!existsByHhId(e.getHhId())) {
                save(e);
            }
        });
    }

    public void save(EmployerEntity entity) {
        if (entity == null) {
            return;
        }
        repository.save(entity);
    }

    public boolean existsByHhId(String hhId) {
        if (hhId == null) {
            return false;
        }
        return repository.existsByHhId(hhId);
    }

    @Override
    public List<EmployerEntity> toEmployerEntityList(ListDto<VacancyRs> list) {
        return mapper.toEmployerEntityList(list.items().stream().map(VacancyRs::employer).toList());
    }

    @Override
    public void saveNewEmployers(ListDto<VacancyRs> list) {
        List<EmployerEntity> entityList = toEmployerEntityList(list);
        saveAllIfNotExist(entityList);
    }

}
