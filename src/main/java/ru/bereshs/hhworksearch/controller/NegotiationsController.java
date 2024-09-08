package ru.bereshs.hhworksearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.bereshs.hhworksearch.mapper.VacancyRsMapper;
import ru.bereshs.hhworksearch.openfeign.hhapi.NegotiationsFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationRs;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.PathParams;
import ru.bereshs.hhworksearch.service.VacancyEntityService;

@Controller
@RequiredArgsConstructor
public class NegotiationsController {
    private final NegotiationsFeignClient negotiationsFeignClient;
    private final VacancyEntityService vacancyEntityService;
    private final VacancyRsMapper mapper;


    @Operation(summary = "Получение списка откликов")
    @GetMapping("/api/negotiations")
    public ListDto<NegotiationRs> getNegotiationsList() {
        return negotiationsFeignClient.getAllNegotiations(PathParams.builder().build());
    }


    @Operation(summary = "Обработка сообщений")
    @PostMapping("/api/negotiations")
    public String updateNegotiations() {
        var negotiationsList = negotiationsFeignClient.getAllNegotiations(PathParams.builder().build()).items().stream().map(mapper::toVacancyEntity).toList();
        vacancyEntityService.updateVacancyStatusList(negotiationsList);
        return "ok";
    }

}
