package ru.bereshs.hhworksearch.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;

import ru.bereshs.hhworksearch.mapper.VacancyMapper;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhNegotiationsDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.dto.ReportDto;
import ru.bereshs.hhworksearch.repository.VacancyEntityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VacancyEntityService {

    private final VacancyEntityRepository vacancyEntityRepository;
    private final VacancyMapper mapper;
    private final DailyReportService reportService;

    public List<VacancyEntity> getVacancyEntityByTimeStampAfter(LocalDateTime date) {
        return vacancyEntityRepository.getVacancyEntitiesByTimeStampAfter(date);
    }

    public String getDaily() {
        var vacancyEntities = getVacancyEntityByTimeStampAfter(LocalDateTime.now().minusDays(1));

        ReportDto reportDto = reportService.getReportDto(vacancyEntities);

        return reportService.getString(reportDto);
    }

    public Optional<VacancyEntity> getByHhId(String hhId) {
        return vacancyEntityRepository.getByHhId(hhId);
    }

    @Loggable
    public List<VacancyEntity> getUnique(List<VacancyEntity> vacancyList) {
        return vacancyList.stream().filter(element -> vacancyEntityRepository.getByHhId(element.getHhId()).isEmpty()).toList();
    }

    public void saveAll(List<VacancyEntity> vacancyEntityList) {
        for (VacancyEntity element : vacancyEntityList) {
            VacancyEntity vacancy = getByHhId(element.getHhId()).orElse(element);
            mapper.updateVacancyEntity(vacancy, element);
            save(vacancy);
        }
    }

    public VacancyEntity getByVacancyDto(HhVacancyDto vacancyDto) {
        var vacancyOpt = getById(vacancyDto.getId());
        return vacancyOpt.orElseGet(() -> createNewVacancy(vacancyDto));
    }

    private VacancyEntity createNewVacancy(HhVacancyDto vacancyDto) {
        return mapper.toVacancyEntity(vacancyDto);
    }

    public void updateVacancyStatusFromNegotiationsList(List<VacancyEntity> list) {
        list.forEach(element -> {
            VacancyEntity entity = getByHhId(element.getHhId()).orElse(element);
            mapper.updateVacancyEntity(entity, element);
            save(entity);
        });
    }


    public Optional<VacancyEntity> getById(String id) {
        return vacancyEntityRepository.getByHhId(id);
    }

    public void save(VacancyEntity vacancy) {
        vacancy.setTimeStamp(LocalDateTime.now());
        vacancyEntityRepository.save(vacancy);
    }

    public void changeAllStatus(List<VacancyEntity> list, VacancyStatus status) {
        list.forEach(element -> changeVacancyStatus(element, status));
    }

    private void changeVacancyStatus(VacancyEntity element, VacancyStatus status) {
        var vacancyOpt = getByHhId(element.getHhId());
        vacancyOpt.ifPresent(vacancy -> {
            vacancy.setStatus(status);
            save(vacancy);
        });
    }

    public List<VacancyEntity> getAll() {
        return vacancyEntityRepository.findAll();
    }
}
