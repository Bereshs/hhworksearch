package ru.bereshs.hhworksearch.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.mapper.SkillMapper;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.repository.SkillsEntityRepository;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.service.SkillEntityService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SkillEntityServiceImpl implements SkillEntityService {
    private final SkillsEntityRepository repository;
    private final SkillMapper mapper;

    @Override
    public Integer getCompliancePercent(VacancyEntity vacancy) {
        List<String> skillList = vacancy.getSkillStringList() != null ? List.of(vacancy.getSkillStringList().split(",")) : new ArrayList<>();
        List<String> suiteList = findAll().stream().map(SkillEntity::getName).filter(skillList::contains).toList();
        int totalSkills = skillList.size();
        int suiteSkills = suiteList.size() * 100;
        return totalSkills > 0 ? suiteSkills / totalSkills : 0;
    }

    @Override
    public List<SkillEntity> getSkillEntityList(List<String> skills) {
        return skills.stream().map(e -> {
            Optional<SkillEntity> optional = getSkillEntityByName(e);
            return optional.orElse(null);
        }).filter(Objects::nonNull).toList();
    }

    public Optional<SkillEntity> getSkillEntityByName(String name) {
        return repository.getSkillEntityByName(name);
    }

    public Optional<SkillEntity> getById(Long id) throws HhWorkSearchException {
        if (id == null) {
            throw new HhWorkSearchException("Wrong id");
        }
        return repository.findById(id);
    }

    public void update(Long id, SkillEntity entity) throws HhWorkSearchException {
        if (id == null || entity == null) {
            throw new HhWorkSearchException("Wrong parameters");
        }
        SkillEntity entityDb = getById(id).orElse(new SkillEntity());
        mapper.updateSkillEntity(entityDb, entity);
        save(entityDb);
    }

    public void save(SkillEntity entity) throws HhWorkSearchException {
        if (entity == null) {
            throw new HhWorkSearchException("Wrong parameter");
        }
        repository.save(entity);
    }


    public List<SkillEntity> findAll() {
        return repository.findAll();
    }

    public void delete(SkillEntity skill) throws HhWorkSearchException {
        if (skill == null) {
            throw new HhWorkSearchException("Wrong parameters");
        }
        repository.delete(skill);
    }


    public List<String> foundAllSkills(VacancyEntity vacancy) {
        Set<String> result;
        if (vacancy.getSkillStringList() != null && vacancy.getSkillStringList().length() > 2) {

            result = new TreeSet<>(Set.of(Arrays.stream(vacancy.getSkillStringList().split(",")).distinct().collect(Collectors.joining())));
        } else {
            result = new TreeSet<>();
        }
        result.addAll(foundAllSkills(vacancy.getName()));
        result.addAll(foundAllSkills(vacancy.getDescription()));
        return result.stream().distinct().toList();
    }

    public List<String> foundAllSkills(String text) {
        if (text == null) {
            return new ArrayList<>();
        }
        List<SkillEntity> skills = findAll();
        return skills.stream().map(SkillEntity::getName).filter(text::contains).distinct().toList();
    }

}
