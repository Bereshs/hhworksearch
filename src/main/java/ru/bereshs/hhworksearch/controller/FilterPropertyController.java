package ru.bereshs.hhworksearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.bereshs.hhworksearch.model.FilterEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.dto.FilterDto;
import ru.bereshs.hhworksearch.service.FilterEntityService;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(   name = "Фильтры",
        description = "Работа с фильтрами для вакансий")
public class FilterPropertyController {

    private final FilterEntityService<VacancyEntity> filterEntityService;

    @Deprecated
    @Operation(summary = "Получение списка всех фильтров")
    @GetMapping("/api/filter")
    public List<FilterEntity> getAllFilter() {
        return filterEntityService.getAll();
    }

    @Deprecated
    @Operation(summary = "Запись нового фильтра")
    @PostMapping ("/api/filter")
    public String addFilter(@RequestBody FilterDto filterDto) {
        filterEntityService.addToFilter(filterDto);
        return "ok";
    }

    @Deprecated
    @Operation(summary = "Удаление фильтра")
    @DeleteMapping("/api/filter")
    public String removeFilter(@RequestBody FilterDto filterDto) {
        filterEntityService.removeFromFilter(filterDto);
        return "ok";
    }

}
