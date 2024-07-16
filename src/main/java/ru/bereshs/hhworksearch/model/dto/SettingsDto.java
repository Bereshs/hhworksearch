package ru.bereshs.hhworksearch.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bereshs.hhworksearch.model.SettingsEntity;

@Getter
@Setter
@NoArgsConstructor
public class SettingsDto {
    private String name;
    private String value;
}
