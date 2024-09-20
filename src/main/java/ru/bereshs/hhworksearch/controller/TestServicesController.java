package ru.bereshs.hhworksearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.openfeign.hhapi.VacancyFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.PathParams;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;
import ru.bereshs.hhworksearch.service.GetVacanciesSchedulerService;
import ru.bereshs.hhworksearch.service.VacancyClientService;
import ru.bereshs.hhworksearch.service.impl.NegotiationsClientService;
import ru.bereshs.hhworksearch.service.impl.ResumeClientService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestServicesController {

    private final VacancyClientService service;

    private final ResumeClientService resumeClientService;

    private final NegotiationsClientService negotiationsClientService;

    private final GetVacanciesSchedulerService schedulerService;
    private final VacancyClientService vacancyClientService;
    private final VacancyFeignClient client;

    @GetMapping("/api/test")
    VacancyRs getVacancyEntityList() throws HhWorkSearchException {

/*
        ResumeEntity resume = resumeClientService.getDefaultResume();
        List<VacancyRs> list =
                vacancyClientService.getPageSimilarVacancies(resume.getHhId(), PathParams.builder().build()).items();

        List<VacancyEntity> full = vacancyClientService.updateOnClient(vacancyClientService.toVacancyEntity(list));
        List<VacancyEntity> filtered = vacancyClientService.filterList(full);

        vacancyClientService.saveAll(filtered);
        //     List<VacancyEntity> list = vacancyClientService.getVacancyWithStatus(VacancyStatus.FOUND);

*/


        //  negotiationsClientService.postNegotiations(list);

        //   schedulerService.dailyHourRequest();

//        List<VacancyEntity> found = vacancyClientService.getVacancyWithStatus(VacancyStatus.FOUND);


//        negotiationsClientService.postNegotiations(found);

//        vacancyClientService.updateStatusVacancies(found, VacancyStatus.REQUEST);

        VacancyRs vacancyRs = vacancyClientService.getOnClientByHhId("106290730");

        List<VacancyEntity> list =  vacancyClientService.getVacancyWithStatus(VacancyStatus.FOUND);
//        service.postNegotiations(list);
        vacancyClientService.updateStatusVacancies(list, VacancyStatus.REQUEST);

        return vacancyRs;
    }
}
