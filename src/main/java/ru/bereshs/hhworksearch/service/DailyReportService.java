package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.dto.ReportDto;

import java.util.List;

public interface DailyReportService {
    ReportDto getReportDto(List<VacancyEntity> vacancyEntities);
    String getString(ReportDto reportDto);
    String getDaily();
}
