package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;

import java.util.List;

public interface EmployerEntityService {
    EmployerEntity getByHhId(String hhId);
    void saveAllIfNotExist(List<EmployerEntity> list);
    void save(EmployerEntity entity);

    boolean existsByHhId(String hhId);

    List<EmployerEntity> toEmployerEntityList(ListDto<VacancyRs> list);

    void saveNewEmployers(ListDto<VacancyRs> list);
}
