package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.model.EmployerEntity;

import java.util.List;

public interface EmployerEntityService {
    EmployerEntity getByHhId(String hhId);
    List<EmployerEntity> extractEmployers(List<HhVacancyDto> list);
    void saveAll(List<EmployerEntity> list);
    void save(EmployerEntity entity);

    boolean existsByHhId(String hhId);

}
