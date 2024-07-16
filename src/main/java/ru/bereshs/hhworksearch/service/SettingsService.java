package ru.bereshs.hhworksearch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.mapper.AppMapper;
import ru.bereshs.hhworksearch.model.SettingsEntity;
import ru.bereshs.hhworksearch.model.dto.SettingsDto;
import ru.bereshs.hhworksearch.repository.SettingsRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final AppMapper mapper;

    public Optional<SettingsEntity> getByName(String name) {
        return settingsRepository.getByName(name);
    }

    public String getValueByName(String name) {
        var result = settingsRepository.getByName(name);
        if (result.isEmpty()) return "";
        return result.get().getValue();

    }

    public void updateParameter(SettingsDto settingsDto) {
        Optional<SettingsEntity> settingsEntity = getByName(settingsDto.getName());
        if (settingsEntity.isPresent()) {
            var entity = settingsEntity.get();
            entity.setValue(settingsDto.getValue());
            save(entity);
            return;
        }

        save(mapper.toSettingsEntity(settingsDto));
    }

    public List<SettingsEntity> getAll() {
        return settingsRepository.findAll();
    }

    public void save(SettingsEntity entity) {
        settingsRepository.save(entity);
    }

    public boolean isDemonActive() {
        var result = settingsRepository.getByName("app.demon-active");
        return result.filter(entity -> Boolean.parseBoolean(entity.getValue())).isPresent();
    }
    public String getAppHHUserAgent() {
        return getValueByName("app.hh-user-agent");
    }

    public String getAppTelegramToken() {
        return getValueByName("app.telegram.token");
    }

}
