package ru.bereshs.hhworksearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.exception.HhworkSearchTokenException;
import ru.bereshs.hhworksearch.hhapiclient.dto.*;
import ru.bereshs.hhworksearch.mapper.VacancyMapper;
import ru.bereshs.hhworksearch.model.FilterScope;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.producer.KafkaProducer;
import ru.bereshs.hhworksearch.service.*;
import ru.bereshs.hhworksearch.service.impl.SkillEntityServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
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
    private final VacancyMapper mapper;
    private final SkillEntityServiceImpl skillsEntityService;
    private final VacancyFilterService vacancyFilterService;


    @Operation(summary = "Получение списка откликов")
    @GetMapping("/api/negotiations")
    public HhListDto<HhNegotiationsDto> getNegotiationsList() throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        return service.getHhNegotiationsDtoList();
    }


    @Operation(summary = "Обработка сообщений")
    @PostMapping("/api/negotiations")
    public String updateNegotiations() throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        var negotiationsList = service.getHhNegotiationsDtoList().getItems().stream().map(mapper::toVacancyEntity).toList();
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
    public String hourScheduler() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException, HhworkSearchTokenException {
        schedulerService.dailyLightTaskRequest();
        return "ok";
    }

    @Operation(summary = "Отчет в 18:30")
    @GetMapping("/api/negotiations/18")
    public String dailyRecommendedScheduler() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException, HhworkSearchTokenException {
        schedulerService.dailyRecommendedRequest();
        return "ok";
    }

    @Operation(summary = "Отчет в 19:30")
    @GetMapping("/api/negotiations/19")
    public String dailyFullScheduler() throws InterruptedException, IOException, ExecutionException, HhWorkSearchException, HhworkSearchTokenException {
        schedulerService.dailyFullRequest();
        return "ok";
    }

    @GetMapping("/api/test")
    public VacancyEntity getVacancyEntity() throws HhworkSearchTokenException, IOException, ExecutionException, InterruptedException {

        List<HhVacancyDto> list  = service.getRecommendedVacancy("java");

        HhVacancyDto vacancyDto = service.getVacancyById("104059180");



        VacancyEntity vacancy = mapper.toVacancyEntity(vacancyDto);

   //     System.out.println(vacancy.getSkillStringList());
        return
//                mapper.toVacancyEntity(dto);
                vacancy;
//        mapper.toVacancyEntity(vacancy);
    }


}
