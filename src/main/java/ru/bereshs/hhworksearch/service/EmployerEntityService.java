package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;

import java.util.List;
import java.util.Optional;

public interface EmployerEntityService {
    Optional<EmployerEntity> getByHhId(String hhId);
    void save(EmployerEntity entity);


    void saveAll(List<EmployerEntity> list);
}
