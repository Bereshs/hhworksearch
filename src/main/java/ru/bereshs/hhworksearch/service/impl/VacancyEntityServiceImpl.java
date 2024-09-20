package ru.bereshs.hhworksearch.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;

import ru.bereshs.hhworksearch.mapper.VacancyMapper;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.repository.VacancyEntityRepository;
import ru.bereshs.hhworksearch.service.VacancyClientService;
import ru.bereshs.hhworksearch.service.VacancyEntityService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VacancyEntityServiceImpl implements VacancyEntityService {

    private final VacancyEntityRepository vacancyEntityRepository;
    private final VacancyMapper mapper;

    public List<VacancyEntity> getVacancyEntityByTimeStampAfter(LocalDateTime date) {
        return vacancyEntityRepository.getVacancyEntitiesByTimeStampAfter(date);
    }


    public Optional<VacancyEntity> getByHhId(String hhId) {

        return Optional.ofNullable(vacancyEntityRepository.getByHhId(hhId));
    }


    public void saveAll(List<VacancyEntity> vacancyEntityList) {
        for (VacancyEntity element : vacancyEntityList) {
            VacancyEntity vacancy = getByHhId(element.getHhId()).orElse(element);
            mapper.updateVacancyEntity(vacancy, element);
            save(vacancy);
        }
    }

    public void setStatusOnList(List<VacancyEntity> list, VacancyStatus status) {
        list.forEach(e -> e.setStatus(status));
    }

    public void updateVacancyStatusList(List<VacancyEntity> list) {
        list.forEach(element -> {
            VacancyEntity entity = getByHhId(element.getHhId()).orElse(element);
            if (!element.getStatus().equals(entity.getStatus())) {
                mapper.updateVacancyEntity(entity, element);
                save(entity);

            }
        });
    }


    public void save(VacancyEntity vacancy) {
        vacancy.setTimeStamp(LocalDateTime.now());
        vacancyEntityRepository.save(vacancy);
    }


    public List<VacancyEntity> getAll() {
        return vacancyEntityRepository.findAll();
    }

    @Override
    public List<VacancyEntity> getVacancyWithStatus(VacancyStatus vacancyStatus) {
        return vacancyEntityRepository.findVacancyEntitiesByStatus(vacancyStatus);
    }
}
