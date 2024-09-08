package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;

import java.util.List;

public interface EmployerEntityService {
    EmployerEntity getByHhId(String hhId);
    void save(EmployerEntity entity);


    void saveAll(List<EmployerEntity> list);
}
