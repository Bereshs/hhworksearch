package ru.bereshs.hhworksearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.repository.FilterEntityRepository;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.model.dto.FilterDto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class FilterEntityService<E extends FilteredVacancy> {
    private final FilterEntityRepository filterEntityRepository;

    @Autowired
    public FilterEntityService(FilterEntityRepository filterEntityRepository) {
        this.filterEntityRepository = filterEntityRepository;
    }

    public List<FilterEntity> getAll() {
        return filterEntityRepository.findAll();
    }

    @Loggable
    public List<E> doFilterNameAndExperience(List<E> vacancyEntities) {
        return vacancyEntities.stream().filter(this::isValid).toList();
    }

    public FilterEntity getById(Long id) throws HhWorkSearchException {
        return filterEntityRepository.findById(id).orElseThrow(() -> new HhWorkSearchException("Wrong id"));
    }

    public List<E> doFilterDescription(List<E> vacancyEntities) {
        return vacancyEntities.stream().filter(this::isValidDescription).toList();
    }

    public boolean isValid(E filteredVacancy) {
        return !isContainWordsScope(filteredVacancy.getName(), getScope(FilterScope.NAME))
                && !isContainWordsScope(filteredVacancy.getExperience(), getScope(FilterScope.EXPERIENCE))
                && !isEqual(filteredVacancy.getEmployer().getId(), getScope(FilterScope.EMPLOYER))
                && isSalaryCurrencyCurrent(filteredVacancy);

    }

    public boolean isValidDescription(E filteredVacancy) {
        if (filteredVacancy.getDescription().length() < 10) {
            return true;
        }
        return isContainWordsScope(filteredVacancy.getDescription(), getScope(FilterScope.KEY))
                && !isContainWordsScope(filteredVacancy.getDescription(), getScope(FilterScope.DESCRIPTION));
    }

    public boolean isSalaryCurrencyCurrent(E filteredVacancy) {
        if (filteredVacancy.getSalary() == null || filteredVacancy.getSalary().getCurrency() == null) return true;
        return filteredVacancy.getSalary().getCurrency().equals("RUR");
    }


    boolean isEqual(String line, List<FilterEntity> scopeName) {
        if (line == null) return false;
        String lineInLowerCase = line.toLowerCase();
        for (FilterEntity name : scopeName) {
            if (lineInLowerCase.equals(name.getWord().toLowerCase())) {
                return true;
            }
        }
        return false;

    }

    public String getKey() {
        return getScope(FilterScope.KEY).get(0).getWord();
    }


    boolean isContainWordsScope(String line, List<FilterEntity> scopeName) {
        String lineInLowerCase = line.toLowerCase();
        for (FilterEntity name : scopeName) {
            if (lineInLowerCase.contains(name.getWord().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    List<FilterEntity> getScope(FilterScope scope) {
        return filterEntityRepository.findFilterEntityByScope(scope);
    }

    public void addToFilter(FilterDto filterDto) {
        List<FilterEntity> filterEntities = getListFromDto(filterDto);
        filterEntities.forEach(this::addIfNotExist);
    }

    public void addIfNotExist(FilterEntity filterEntity) {
        var optional = filterEntityRepository.findFilterEntityByScopeAndWord(filterEntity.getScope(), filterEntity.getWord());
        if (optional.isEmpty()) {
            save(filterEntity);
        }
    }

    public void save(FilterEntity filterEntity) {
        filterEntityRepository.save(filterEntity);
    }

    public void removeFromFilter(FilterDto filterDto) {
        List<FilterEntity> filterEntities = getListFromDto(filterDto);
        filterEntityRepository.deleteAll(
                getListFromRepository(filterEntities)
        );
    }

    public List<FilterEntity> getListFromRepository(List<FilterEntity> filterEntities) {
        return filterEntities.stream().map(element -> filterEntityRepository.findFilterEntityByScopeAndWord(element.getScope(), element.getWord()).orElse(null)).filter(Objects::nonNull).toList();
    }

    private List<FilterEntity> getListFromDto(FilterDto filterDto) {
        List<String> listWords = filterDto.getWords();
        return listWords.stream().map(word -> {
            FilterEntity filterEntity = new FilterEntity();
            filterEntity.setScope(FilterScope.valueOf(filterDto.getScope().toLowerCase()));
            filterEntity.setWord(word.toLowerCase());
            return filterEntity;
        }).toList();
    }


    public void update(Long id, FilterEntity filterEntity) throws HhWorkSearchException {
        if (id == null) {
            throw new HhWorkSearchException("Wrong id");
        }
        if (filterEntity.getWord() == null) {
            throw new HhWorkSearchException("Wrong word");
        }
        FilterEntity filterDb = getById(id);
        filterDb.setWord(filterEntity.getWord());
        save(filterEntity);
    }

    public void delete(FilterEntity entity) {
        filterEntityRepository.delete(entity);
    }
}
