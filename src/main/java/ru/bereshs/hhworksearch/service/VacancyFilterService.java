package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.model.FilterEntity;
import ru.bereshs.hhworksearch.model.FilterScope;
import ru.bereshs.hhworksearch.model.VacancyEntity;

import java.util.List;

public interface VacancyFilterService {

    boolean isContainsKey(VacancyEntity vacancy);

    boolean isSuitVacancy(VacancyEntity vacancy);

    List<FilterEntity> getScopeElements(FilterScope scope);

    boolean isWordNotContains(List<FilterEntity> list, String word);

    boolean isSuitName(String name);

    boolean isSuitExperience(String experience);

    boolean isSuitEmployer(String employer);

    boolean isSuitDescription(String description);

    String getFilterResult(VacancyEntity vacancy);


}
