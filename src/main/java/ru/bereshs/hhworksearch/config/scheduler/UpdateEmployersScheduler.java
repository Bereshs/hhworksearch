package ru.bereshs.hhworksearch.config.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.service.EmployerEntityService;
import ru.bereshs.hhworksearch.service.VacancyClientService;
import ru.bereshs.hhworksearch.service.impl.EmployerClientService;

import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class UpdateEmployersScheduler {
    private final EmployerClientService employerClientService;

    private final VacancyClientService vacancyClientService;
    @Scheduled(cron = "0 2 9-18 * * *")
    public void scheduleDayLightTask()  {
        List<EmployerEntity> list = vacancyClientService.getVacancyWithStatus(VacancyStatus.FOUND).stream()
                .map(e->{
                    String hhId = e.getEmployerId();
                    return employerClientService.getByHhId(hhId);
                }).filter(e->e.getName()==null || e.getName().length()<2).toList();

        List<EmployerEntity> updated = employerClientService.updateOnClient(list);

      employerClientService.saveAll(updated);
    }

}
