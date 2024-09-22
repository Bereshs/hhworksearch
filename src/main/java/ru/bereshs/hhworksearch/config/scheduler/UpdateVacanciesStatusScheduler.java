package ru.bereshs.hhworksearch.config.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationRs;
import ru.bereshs.hhworksearch.service.VacancyClientService;
import ru.bereshs.hhworksearch.service.impl.NegotiationsClientService;

import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class UpdateVacanciesStatusScheduler {

    private final NegotiationsClientService negotiationsClientService;
    private final VacancyClientService vacancyClientService;

    @Scheduled(cron = "0 3 8-20 * * *")
    public void scheduleUpdateVacancies() {
        List<NegotiationRs> list = negotiationsClientService.getAllNegotiations().items().stream().filter(e ->
                !e.state().id().equalsIgnoreCase(VacancyStatus.RESPONSE.name())).toList();
        vacancyClientService.updateVacancyStatus(list);

    }


}
