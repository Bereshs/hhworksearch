package ru.bereshs.hhworksearch.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SearchVacancyRequestDto {
    private List<String> relevantExperience;
    private List<String> excludeWords;
    private List<String> requestWords;
}
