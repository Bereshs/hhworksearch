package ru.bereshs.hhworksearch.controller.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.bereshs.hhworksearch.mapper.VacancyMapper;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReportController {

    private final VacancyEntityService service;
    private final EmployerEntityService employerService;
    private final VacancyMapper mapper;
    private final VacancyFilterService vacancyFilterService;
    private final SkillEntityService skillEntityService;


    @GetMapping("/report")
    String getReportPage(Model model) {
        List<VacancyEntity> list =
                service.getVacancyEntityByTimeStampAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)).stream()
                        .peek(e -> {
                            e.setFilterResult(vacancyFilterService.getFilterResult(e));
                            e.setPercent(skillEntityService.getCompliancePercent(e));
                        }).toList();

        model.addAttribute("list",
                mapper.toVacancyDtoList(list).stream()
                        .peek(e -> mapper.updateVacancyDto(e, employerService.getByHhId(e.getEmployerId()).orElse(new EmployerEntity()))).toList());

        return "report";
    }
}
