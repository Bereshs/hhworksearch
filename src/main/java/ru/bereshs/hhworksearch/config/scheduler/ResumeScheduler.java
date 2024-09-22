package ru.bereshs.hhworksearch.config.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.service.impl.ResumeClientService;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ResumeScheduler {

    private final ResumeClientService service;

    @Scheduled(cron = "0 58 8-17 * * *")
    public void scheduleDayUpdateResume()  {
        service.updateResume();
    }
}
