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

    private final NegotiationsFeignClient negotiationsFeignClient;

    private final ResumeClientService resumeClientService;
    private final VacancyClientService vacancyClientService;

    private final FilterEntityService filterEntityService;


    @Loggable
    public void dailyHourRequest() {
        ResumeEntity defaultResume = resumeClientService.getDefaultResume();
        List<VacancyEntity> list = vacancyClientService.toVacancyEntity(
                vacancyClientService.getPageSimilarVacancies(defaultResume.getHhId(), PathParams.builder().build()).items()
        );

        List<VacancyEntity> full = vacancyClientService.updateOnClient(list);
        List<VacancyEntity> filtered = vacancyClientService.filterList(full);

        vacancyClientService.saveAll(filtered);
//        postNegotiations(filtered);

//        vacancyClientService.updateStatusVacancies(list, VacancyStatus.REQUEST);
    }

    @Loggable
    public void dailyFullRequest() {
        ResumeEntity defaultResume = resumeClientService.getDefaultResume();
        List<VacancyEntity> list = vacancyClientService.getAllPageSimilarVacancies(defaultResume.getHhId());
        List<VacancyEntity> filtered = vacancyClientService.filterList(list);

        //      postNegotiations(filtered);

        vacancyClientService.updateVacancyStatus(negotiationsFeignClient.getAllNegotiations().items());
        vacancyClientService.saveAll(filtered);
    }


    @Loggable
    public void dailySearchRequest() {

        List<VacancyEntity> list = vacancyClientService.getAllPageVacancies(filterEntityService.getKey());
        List<VacancyEntity> filtered = vacancyClientService.filterList(list);

        vacancyClientService.saveAll(filtered);

        //    postNegotiations(filtered);

        //    vacancyClientService.updateStatusVacancies(filtered, VacancyStatus.REQUEST);
    }


}
