package ru.bereshs.hhworksearch.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.repository.SkillsEntityRepository;
import ru.bereshs.hhworksearch.model.FilteredVacancy;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhListDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SkillsEntityService {
    private final SkillsEntityRepository skillsEntityRepository;

    public SkillEntity getById(long id) throws HhWorkSearchException {
        return skillsEntityRepository.findById(id).orElseThrow(() -> new HhWorkSearchException("Skill id: " + id + " not found in database"));
    }

    public SkillEntity getByName(String name) throws HhWorkSearchException {
        return skillsEntityRepository.getSkillsEntityByName(name).orElseThrow(() -> new HhWorkSearchException("Skill name: " + name + " not found in database"));
    }

    public void update(Long id, SkillEntity entity) throws HhWorkSearchException {
        SkillEntity entityDb;
        if (id.equals(0L)) {
            entityDb = new SkillEntity();
        } else {
            entityDb = getById(id);
        }

        if (entity.getName() != null && !entity.getName().equals(entityDb.getName())) {
            entityDb.setName(entity.getName());
        }

        if (entity.getDescription() != null && !entity.getDescription().equals(entityDb.getDescription())) {
            entityDb.setDescription(entity.getDescription());
        }
        save(entityDb);
    }

    public void save(SkillEntity entity) {
        skillsEntityRepository.save(entity);
    }

    public void saveAll(List<SkillEntity> entityList) {
        skillsEntityRepository.saveAll(entityList);
    }


    public List<SkillEntity> findAll() {
        return skillsEntityRepository.findAll();
    }

    public HhListDto<SkillEntity> getAll() {
        HhListDto<SkillEntity> result = new HhListDto<>();
        List<SkillEntity> items = findAll();
        result.setFound(items.size());
        result.setPage(0);
        result.setPages(1);
        result.setItems(items);
        return result;
    }

    public void patchSkillEntityById(long id, SkillEntity entityDto) throws HhWorkSearchException {
        SkillEntity entity = getById(id);
        entity.setName(entityDto.getName().toLowerCase());
        entity.setDescription(entityDto.getDescription());
        save(entity);
    }

    public List<SkillEntity> updateList(List<SkillEntity> list) {
        for (SkillEntity skillEntity : list) {
            try {
                SkillEntity skill = getByName(skillEntity.getName().toLowerCase());
                skillEntity.setDescription(skill.getDescription());
            } catch (HhWorkSearchException ex) {
                log.info("skill " + skillEntity.getName() + " not found");
            }
        }
        return list.stream().filter(element -> element.getDescription() != null).toList();
    }


    public List<SkillEntity> extractVacancySkills(FilteredVacancy vacancy) {
        List<SkillEntity> skills = findAll();
        List<SkillEntity> result = new ArrayList<>();
        result.addAll(extractSkillsFromList(vacancy.getSkillStringList(), skills));
        result.addAll(extractSkillsFromString(vacancy.getDescription(), skills));
        result.addAll(extractSkillsFromString(vacancy.getName(), skills));
        return result.stream().distinct().collect(Collectors.toList());
    }

    private List<SkillEntity> extractSkillsFromList(List<String> list, List<SkillEntity> skillEntities) {
        List<SkillEntity> result = new ArrayList<>();
        if (list == null) {
            return result;
        }
        skillEntities.forEach(element -> {
            if (list.contains(element.getName())) {
                result.add(element);
            }
        });
        return result;
    }

    private List<SkillEntity> extractSkillsFromString(String list, List<SkillEntity> skillEntities) {
        List<SkillEntity> result = new ArrayList<>();
        skillEntities.forEach(element -> {
            if (list.toLowerCase().contains(element.getName().toLowerCase())) {
                result.add(element);
            }
        });
        return result;
    }

    public void delete(SkillEntity skill) {
        skillsEntityRepository.delete(skill);
    }
}
