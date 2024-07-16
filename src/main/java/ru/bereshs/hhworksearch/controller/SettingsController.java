package ru.bereshs.hhworksearch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.bereshs.hhworksearch.mapper.AppMapper;
import ru.bereshs.hhworksearch.model.dto.SettingsDto;
import ru.bereshs.hhworksearch.service.SettingsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService service;
    private final AppMapper mapper;

    @PostMapping("/api/settings")
    public String setParameter (@RequestBody SettingsDto settingsDto) {
        service.updateParameter(settingsDto);
        return "ok";
    }

    @GetMapping("/api/settings")
    public List<SettingsDto> getSettings () {
        return service.getAll().stream().map(mapper::toSettingsDto).toList();
    }

    @GetMapping("/api/settings/demon")
    public boolean isDemonActive() {
        return service.isDemonActive();
    }
}
