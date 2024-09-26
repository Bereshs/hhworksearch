package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VacancyEntityService {
    List<VacancyEntity> getVacancyEntityByTimeStampAfter(LocalDateTime date);
    Optional<VacancyEntity> getByHhId(String hhId);
    void saveAll(List<VacancyEntity> vacancyEntityList);
    void updateVacancyStatusList(List<VacancyEntity> list);
    void save(VacancyEntity vacancy);
    List<VacancyEntity> getAll();
    List<VacancyEntity> getVacancyWithStatus(VacancyStatus vacancyStatus);
}
