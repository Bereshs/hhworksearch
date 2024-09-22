package ru.bereshs.hhworksearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.openfeign.hhapi.NegotiationsFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.*;
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

        List<VacancyEntity> full = vacancyClientService.updateOnClient(list);
        List<VacancyEntity> filtered = vacancyClientService.filterList(full);
        vacancyClientService.saveAll(filtered);
    }

    @Loggable
    public void dailyFullSearchSimilar() {
        ResumeEntity defaultResume = resumeClientService.getDefaultResume();
        List<VacancyEntity> list = vacancyClientService.getAllPageSimilarVacancies(defaultResume.getHhId());
        List<VacancyEntity> filtered = vacancyClientService.filterList(list);
        vacancyClientService.saveAll(filtered);
    }


    @Loggable
    public void dailySearchAll() {

        List<VacancyEntity> list = vacancyClientService.getAllPageVacancies(filterEntityService.getKey());
        List<VacancyEntity> filtered = vacancyClientService.filterList(list);
        vacancyClientService.saveAll(filtered);
    }


}
