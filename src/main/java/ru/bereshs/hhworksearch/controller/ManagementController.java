package ru.bereshs.hhworksearch.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.*;
import ru.bereshs.hhworksearch.producer.KafkaProducer;
import ru.bereshs.hhworksearch.service.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@AllArgsConstructor
@Tag(name = "Отклики", description = "Работа с откликами")
public class ManagementController {
    private final HhService service;
    private final VacancyEntityService vacancyEntityService;
    private final KafkaProducer kafkaProducer;
    private final SchedulerService schedulerService;


    @Operation(summary = "Получение списка откликов")
    @GetMapping("/api/negotiations")
    public HhListDto<HhNegotiationsDto> getNegotiationsList() throws IOException, ExecutionException, InterruptedException {
        return service.getHhNegotiationsDtoList();
    }


    @Operation(summary = "Обработка сообщений")
    @PostMapping("/api/negotiations")
    public String updateNegotiations() throws IOException, ExecutionException, InterruptedException {
        var negotiationsList = service.getHhNegotiationsDtoList();
        vacancyEntityService.updateVacancyStatusFromNegotiationsList(negotiationsList);
        return "ok";
    }

    @Operation(summary = "Ежедневный отчет")
    @GetMapping("/api/negotiations/daily")
    public String dailyReport() throws HhWorkSearchException {
        String message = vacancyEntityService.getDaily();
        kafkaProducer.produceDefault(message);
        return message;
    }

    @Operation(summary = "Дневной ежечасный запрос")
    @GetMapping("/api/negotiations/hour")
    public String hourScheduler() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        schedulerService.dailyLightTaskRequest();
        return "ok";
    }

    @Operation(summary = "Отчет в 18:30")
    @GetMapping("/api/negotiations/18")
    public String dailyRecommendedScheduler() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        schedulerService.dailyRecommendedRequest();
        return "ok";
    }

    @Operation(summary = "Отчет в 19:30")
    @GetMapping("/api/negotiations/19")
    public String dailyFullScheduler() throws InterruptedException, IOException, ExecutionException, HhWorkSearchException {
        schedulerService.dailyFullRequest();
        return "ok";
    }


}
