package ru.bereshs.hhworksearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.producer.KafkaProducer;
import ru.bereshs.hhworksearch.service.*;
import ru.bereshs.hhworksearch.service.impl.GetVacanciesSchedulerService;

@RestController
@Slf4j
@AllArgsConstructor
@Tag(name = "Отклики", description = "Работа с откликами")
public class ManagementController {
    private final KafkaProducer kafkaProducer;
    private final GetVacanciesSchedulerService schedulerService;
    private final DailyReportService reportService;



    @Operation(summary = "Ежедневный отчет")
    @GetMapping("/api/negotiations/daily")
    public String dailyReport() throws HhWorkSearchException {
        String message = reportService.getDaily();
        kafkaProducer.produceDefault(message);
        return message;
    }

    @Operation(summary = "Дневной ежечасный запрос")
    @GetMapping("/api/negotiations/hour")
    public String hourScheduler()  {
        schedulerService.dailyHourSearchSimilar();
        return "ok";
    }

    @Operation(summary = "Отчет в 18:30")
    @GetMapping("/api/negotiations/18")
    public String dailyRecommendedScheduler() {
        schedulerService.dailySearchAll();
        return "ok";
    }

    @Operation(summary = "Отчет в 19:30")
    @GetMapping("/api/negotiations/19")
    public String dailyFullScheduler(){
        schedulerService.dailyFullSearchSimilar();
        return "ok";
    }

}
