package ru.bereshs.hhworksearch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.service.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final SchedulerService service;

    @Scheduled(cron = "0 0 9-18 * * *")
    public void scheduleDayLightTask() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        service.dailyLightTaskRequest();
    }

    @Scheduled(cron = "0 30 19 * * *")
    public void scheduleDailyFullRequest() throws InterruptedException, IOException, ExecutionException, HhWorkSearchException {
        service.dailyFullRequest();
    }

    @Scheduled(cron = "0 30 18 * * *")
    public void scheduleDailyRecommendedRequest() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        service.dailyRecommendedRequest();
    }


}
