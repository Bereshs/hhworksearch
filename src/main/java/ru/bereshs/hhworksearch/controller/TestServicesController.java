package ru.bereshs.hhworksearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bereshs.hhworksearch.config.scheduler.GetVacanciesScheduler;
import ru.bereshs.hhworksearch.config.scheduler.UpdateEmployersScheduler;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.EmployerMapper;
import ru.bereshs.hhworksearch.mapper.VacancyRsMapper;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.VacancyFeignClient;
import ru.bereshs.hhworksearch.service.SkillEntityService;
import ru.bereshs.hhworksearch.service.VacancyFilterService;
import ru.bereshs.hhworksearch.service.impl.EmployerClientServiceImpl;
import ru.bereshs.hhworksearch.service.VacancyClientService;
import ru.bereshs.hhworksearch.service.impl.NegotiationsClientService;
import ru.bereshs.hhworksearch.service.impl.ResumeClientService;

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
    private final VacancyRsMapper mapper;
    private final VacancyFilterService vacancyFilterService;
    private final SkillEntityService skillEntityService;

    private final EmployerClientServiceImpl employerClientService;
    private final EmployerMapper employerMapper;
    @GetMapping("/api/test")
    EmployerEntity getVacancyEntityList() throws HhWorkSearchException {



        return employerMapper.toEmployerEntity(employerClientService.getByHhIdOnClient("80"));


    }
}
