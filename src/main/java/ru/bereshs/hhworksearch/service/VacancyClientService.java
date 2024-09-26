package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationRs;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.PathParams;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;

import java.util.List;
import java.util.Optional;

public interface VacancyClientService {

    VacancyRs getOnClientByHhId(String hhId);
    List<VacancyEntity> updateOnClient(List<VacancyEntity> list);

    Optional<VacancyEntity> getByHhId(String hhId);

    ListDto<VacancyRs> getPageVacancies(PathParams params);

    List<VacancyEntity> getAllPageVacancies(String key);

    List<VacancyEntity> getAllPageSimilarVacancies(String resumeId);

    ListDto<VacancyRs> getPageSimilarVacancies(String resumeId, PathParams params);

    List<VacancyEntity> toVacancyEntity(List<VacancyRs> list);

    void updateVacancyStatus(List<NegotiationRs> negotiationRsList);

    List<VacancyEntity> filterList(List<VacancyEntity> list);

    public void saveAll(List<VacancyEntity> list);

    List<VacancyEntity> getVacancyWithStatus(VacancyStatus vacancyStatus);
    void updateStatusVacancy(VacancyEntity vacancy, VacancyStatus status);

    void save(VacancyEntity e);

}
