package ru.bereshs.hhworksearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.stream.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bereshs.hhworksearch.config.scheduler.GetVacanciesScheduler;
import ru.bereshs.hhworksearch.config.scheduler.UpdateEmployersScheduler;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.VacancyRsMapper;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.openfeign.hhapi.VacancyFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationRs;
import ru.bereshs.hhworksearch.service.VacancyFilterService;
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
    private final VacancyRsMapper mapper;
    private final VacancyFilterService vacancyFilterService;

    @GetMapping("/api/test")
   String getVacancyEntityList() throws HhWorkSearchException {

        VacancyEntity vacancy = vacancyClientService.getByHhId("107758644").orElse(null);

        log.info("wpwe {}", vacancyFilterService.isContainsKey(vacancy));
/*


        List<NegotiationRs> negotiationRsList = negotiationsClientService.getAllNegotiations().items().stream().filter(e ->
                !e.state().id().equalsIgnoreCase(VacancyStatus.RESPONSE.name())).toList();
        vacancyClientService.updateVacancyStatus(negotiationRsList);
      //  List<VacancyEntity> listFromNegotiations = negotiationRsList.stream().map(mapper::toVacancyEntity).toList();


        //       log.info("size={}",list.size());

      //  updateEmployersScheduler.scheduleDayLightTask();
//        schedulerService.scheduleDayLightTask();
//*/
        updateEmployersScheduler.scheduleDayLightTask();
        return "listFromNegotiations";
    }
}
