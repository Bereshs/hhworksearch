package ru.bereshs.hhworksearch.config.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.producer.KafkaProducer;
import ru.bereshs.hhworksearch.service.DailyReportService;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ReportScheduler {
    private final DailyReportService reportService;
    private final KafkaProducer producer;

    @Scheduled(cron = "0 0 20 * * *")
    public void scheduleDailyReport() throws HhWorkSearchException {
        producer.produceDefault(reportService.getDaily());
    }
}
