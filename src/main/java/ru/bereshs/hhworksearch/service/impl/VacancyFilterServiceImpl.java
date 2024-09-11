package ru.bereshs.hhworksearch.service.impl;

import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.model.FilterEntity;
import ru.bereshs.hhworksearch.model.FilterScope;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.service.VacancyFilterService;

import java.util.List;

@Service
public class VacancyFilterServiceImpl implements VacancyFilterService {

    private final List<FilterEntity> filter;

    public VacancyFilterServiceImpl(FilterEntityServiceImpl service) {
        this.filter = service.getAll();
    }

    @Override
    public boolean isSuitVacancy(VacancyEntity vacancy) {

        return isSuitName(vacancy.getName())
                && isSuitExperience(vacancy.getExperience())
                && isSuitEmployer(vacancy.getEmployerId())
                && isSuitDescription(vacancy.getDescription());
    }

    public List<FilterEntity> getScopeElements(FilterScope scope) {
        return filter.stream().filter(e -> e.getScope().equals(scope)).toList();
    }

    public boolean isWordNotContains(List<FilterEntity> list, String line) {
        if (line == null) {
            return true;
        }
        return list.stream().noneMatch(e -> line.toLowerCase().contains(e.getWord().toLowerCase()));
    }

    public boolean isWordNotEquals(List<FilterEntity> list, String word) {
        if (word == null) {
            return false;
        }
        return list.stream().noneMatch(e -> e.getWord().equalsIgnoreCase(word));
    }

    public boolean isSuitName(String name) {
        return isWordNotContains(getScopeElements(FilterScope.NAME), name);
    }

    public boolean isSuitExperience(String experience) {
        return isWordNotContains(getScopeElements(FilterScope.EXPERIENCE), experience);
    }

    public boolean isSuitEmployer(String employer) {
        return isWordNotEquals(getScopeElements(FilterScope.EMPLOYER), employer);
    }

    public boolean isSuitDescription(String description) {
        return isWordNotContains(getScopeElements(FilterScope.DESCRIPTION), description);
    }

    @Override
    public String getFilterResult(VacancyEntity vacancy) {
        return (isSuitName(vacancy.getName()) ? "" : "NAME")
                + (isSuitExperience(vacancy.getExperience()) ? "" : " EXPERIENCE")
                + (isSuitEmployer(vacancy.getEmployerId()) ? "" : " EMPLOYER")
                + (vacancy.getDescription() != null && isSuitDescription(vacancy.getDescription()) ? "" : " DESCRIPTION");
    }

}
