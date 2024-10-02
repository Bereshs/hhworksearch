package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.openfeign.hhapi.NegotiationsFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.*;
import ru.bereshs.hhworksearch.service.FilterEntityService;
import ru.bereshs.hhworksearch.service.VacancyClientService;
import ru.bereshs.hhworksearch.service.impl.ResumeClientService;

import java.util.List;

@Slf4j

@Service
@RequiredArgsConstructor
public class GetVacanciesSchedulerService {

    private final ResumeClientService resumeClientService;
    private final VacancyClientService vacancyClientService;
    private final FilterEntityService filterEntityService;


    @Loggable
    public void dailyHourSearchSimilar() {
        ResumeEntity defaultResume = resumeClientService.getDefaultResume();
        List<VacancyEntity> list = vacancyClientService.toVacancyEntity(
                vacancyClientService.getPageSimilarVacancies(defaultResume.getHhId(), PathParams.builder().build()).items()
        );

        List<VacancyEntity> filtered = filterList(list);
        vacancyClientService.saveAll(filtered);
    }

    @Loggable
    public void dailyFullSearchSimilar() {
        ResumeEntity defaultResume = resumeClientService.getDefaultResume();
        List<VacancyEntity> list = vacancyClientService.getAllPageSimilarVacancies(defaultResume.getHhId());
        List<VacancyEntity> filtered = filterList(list);

        vacancyClientService.saveAll(filtered);
    }


    @Loggable
    public void dailySearchAll() {

        List<VacancyEntity> list = vacancyClientService.getAllPageVacancies(filterEntityService.getKey());
        List<VacancyEntity> filtered = filterList(list);
        vacancyClientService.saveAll(filtered);
    }


    public List<VacancyEntity> filterList(List<VacancyEntity> list) {
        List<VacancyEntity> preFiltered = vacancyClientService.filterList(list);
        List<VacancyEntity> unique = preFiltered.stream().filter(e ->
                vacancyClientService.getByHhIdOnService(e.getHhId()).isEmpty()).toList();

        List<VacancyEntity> full = vacancyClientService.updateOnClient(unique);

        return vacancyClientService.filterList(full);
    }
}
