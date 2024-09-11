package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.mapper.EmployerMapper;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
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


    public List<EmployerEntity> extractEmployers(List<HhVacancyDto> list) {
        return list.stream().map(entity -> mapper.toEmployerEntity(entity.getEmployer())).toList();
    }

    public void saveAll(List<EmployerEntity> list) {
        list.stream().filter(employer -> !existsByHhId(employer.getHhId()))
                .forEach(this::save);
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

}
