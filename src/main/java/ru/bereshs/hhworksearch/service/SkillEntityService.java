package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.exception.HhworkSearchTokenException;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;

import java.util.List;
import java.util.Optional;

public interface SkillEntityService {

    List<SkillEntity> getSkillEntityList(List<String> skills);
    Optional<SkillEntity> getById(Long id) throws HhWorkSearchException;
    void update(Long id, SkillEntity entity) throws HhWorkSearchException;

    void save(SkillEntity entity) throws HhWorkSearchException;

    List<SkillEntity> findAll();
    void delete(SkillEntity skill) throws HhWorkSearchException;
    List<String> foundAllSkills(VacancyEntity vacancy);
    List<String> foundAllSkills(String text);

}
