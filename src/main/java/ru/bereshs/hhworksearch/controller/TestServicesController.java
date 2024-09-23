package ru.bereshs.hhworksearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bereshs.hhworksearch.config.scheduler.GetVacanciesScheduler;
import ru.bereshs.hhworksearch.config.scheduler.UpdateEmployersScheduler;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.openfeign.hhapi.VacancyFeignClient;
import ru.bereshs.hhworksearch.service.impl.GetVacanciesSchedulerService;
import ru.bereshs.hhworksearch.service.VacancyClientService;
import ru.bereshs.hhworksearch.service.impl.NegotiationsClientService;
import ru.bereshs.hhworksearch.service.impl.ResumeClientService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestServicesController {

    private final VacancyClientService service;

    private final ResumeClientService resumeClientService;

    private final NegotiationsClientService negotiationsClientService;

    private final GetVacanciesScheduler schedulerService;
    private final VacancyClientService vacancyClientService;
    private final VacancyFeignClient client;
    private final UpdateEmployersScheduler updateEmployersScheduler;

    @GetMapping("/api/test")
   String getVacancyEntityList() throws HhWorkSearchException {



        List<VacancyEntity> list =  vacancyClientService.getVacancyWithStatus(VacancyStatus.FOUND);
        negotiationsClientService.postNegotiations(list);


 //       log.info("size={}",list.size());

      //  updateEmployersScheduler.scheduleDayLightTask();
//        schedulerService.scheduleDayLightTask();
//        updateEmployersScheduler.scheduleDayLightTask();
        return "ok";
    }
}
