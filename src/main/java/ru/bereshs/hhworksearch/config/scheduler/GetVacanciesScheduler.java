package ru.bereshs.hhworksearch.config.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.bereshs.hhworksearch.service.*;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class GetVacanciesScheduler {

    private final GetVacanciesSchedulerService service;

    @Scheduled(cron = "0 0 9-18 * * *")
    public void scheduleDayLightTask()  {
        service.dailyHourSearchSimilar();
    }

    @Scheduled(cron = "0 30 19 * * *")
    public void scheduleDailyFullRequest()  {
        service.dailyFullSearchSimilar();
    }

    @Scheduled(cron = "0 30 18 * * *")
    public void scheduleDailyRecommendedRequest() {
        service.dailySearchAll();
    }



}
