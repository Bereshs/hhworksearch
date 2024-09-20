package ru.bereshs.hhworksearch.config.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.service.VacancyClientService;
import ru.bereshs.hhworksearch.service.impl.NegotiationsClientService;

import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class PostNegotiationsScheduler {

    private final VacancyClientService vacancyClientService;
    private final NegotiationsClientService service;
    @Scheduled(cron = "0 10 8-21 * * *")
    public void scheduleDayPostNegotiations() throws HhWorkSearchException {
        List<VacancyEntity> list =  vacancyClientService.getVacancyWithStatus(VacancyStatus.FOUND);
        service.postNegotiations(list);
        vacancyClientService.updateStatusVacancies(list, VacancyStatus.REQUEST);
    }
}
