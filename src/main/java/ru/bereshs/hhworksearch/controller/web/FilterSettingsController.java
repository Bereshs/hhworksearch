package ru.bereshs.hhworksearch.controller.web;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.bereshs.hhworksearch.mapper.SimpleDtoMapper;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.model.FilterEntity;
import ru.bereshs.hhworksearch.model.FilterScope;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;
import ru.bereshs.hhworksearch.service.EmployerEntityService;
import ru.bereshs.hhworksearch.service.FilterEntityService;

import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
public class FilterSettingsController {
    private final FilterEntityService filterEntityService;
    private final SimpleDtoMapper mapper;
    private final EmployerEntityService employerEntityService;

    @GetMapping("/filtersettings")
    public String settingsPage(Model model) {
        List<FilterEntity> filter = filterEntityService.getAll();
        List<SimpleDto> keyWords = simpleDtoFilter(filter, FilterScope.KEY);

        List<SimpleDto> experience = simpleDtoFilter(filter, FilterScope.EXPERIENCE);

        List<SimpleDto> employer = filter.stream()
                .filter(e -> e.getScope().equals(FilterScope.EMPLOYER))
                .map(e -> {
                    EmployerEntity entity = employerEntityService.getByHhId(e.getWord());
                    if (entity == null) {
                        return new SimpleDto(e.getId(), e.getWord(), null);
                    }
                    entity.setId(e.getId());
                    return mapper.toSimpleDto(entity);
                })
                .sorted(Comparator.comparing(SimpleDto::name, String::compareTo))
                .toList();

        List<SimpleDto> name = simpleDtoFilter(filter, FilterScope.NAME);

        List<SimpleDto> description = simpleDtoFilter(filter, FilterScope.DESCRIPTION);


        model.addAttribute("keyWords", keyWords);
        model.addAttribute("experience", experience);
        model.addAttribute("employer", employer);
        model.addAttribute("name", name);
        model.addAttribute("description", description);


        return "filtersettings";
    }

    List<SimpleDto> simpleDtoFilter(List<FilterEntity> filter, FilterScope scope) {
        return filter.stream()
                .filter(e -> e.getScope().equals(scope))
                .map(mapper::toSimpleDto).sorted(Comparator.comparing(SimpleDto::name, String::compareTo)).toList();
    }
}
