package ru.bereshs.hhworksearch.controller.web;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.bereshs.hhworksearch.config.AppConfig;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.SimpleDtoMapper;
import ru.bereshs.hhworksearch.model.ParameterEntity;
import ru.bereshs.hhworksearch.model.ParameterType;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;
import ru.bereshs.hhworksearch.service.ParameterEntityService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ParameterSettingsController {

    private final ParameterEntityService service;
    private final SimpleDtoMapper mapper;

    private final AppConfig appConfig;


    @GetMapping("/parametersettings")
    String getParameterSettingsPage(Model model) {

        List<ParameterEntity> list = Arrays.stream(ParameterType.values()).map(e ->
        {
            ParameterEntity p = new ParameterEntity();
            p.setType(e);
            p.setId(0L);
            p.setData("Не заполнено");
            try {
                var a = service.getByType(e);
                p.setData("Заполнено");
                p.setId(a.getId());
                return p;
            } catch (HhWorkSearchException ex) {
                return p;
            }
        }).toList();

        model.addAttribute("parameters", mapper.fromListParameter(list));
        model.addAttribute("useragent", new SimpleDto(null, "USER_AGENT", appConfig.getUserAgent()));

        return "parametersettings";
    }
}
