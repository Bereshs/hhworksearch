package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.mapper.EmployerMapper;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;
import ru.bereshs.hhworksearch.repository.EmployerEntityRepository;
import ru.bereshs.hhworksearch.service.EmployerEntityService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerEntityServiceImpl implements EmployerEntityService {

    private final EmployerEntityRepository repository;
    private final EmployerMapper mapper;


    public Optional<EmployerEntity> getByHhId(String hhId) {
        return repository.getByHhId(hhId);
    }


    public void save(EmployerEntity entity) {
        if (entity == null) {
            return;
        }
        repository.save(entity);
    }

    @Override
    public void saveAll(List<EmployerEntity> list) {
        repository.saveAll(list);
    }

}
