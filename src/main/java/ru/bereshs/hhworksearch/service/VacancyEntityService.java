package ru.bereshs.hhworksearch.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.mapper.AppMapper;
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
    private final AppMapper mapper;
    private final DailyReportService reportService;

    public List<VacancyEntity> getVacancyListFromListDto(HhListDto<HhNegotiationsDto> negotiationsList) {
        return negotiationsList.getItems().stream().map(entity -> {
            VacancyEntity vacancy = mapper.toVacancyEntity(entity.getVacancy());
            vacancy.setStatus(entity.getState().getId());
            return vacancy;
        }).toList();
    }

    public void updateStatus(VacancyEntity vacancy, VacancyStatus status) {
        vacancy.setStatus(status);
        save(vacancy);
    }

    public List<VacancyEntity> getVacancyEntityByTimeStampAfter(LocalDateTime date) {
        return vacancyEntityRepository.getVacancyEntitiesByTimeStampAfter(date);
    }

    public String getDaily() {
        var vacancyEntities = getVacancyEntityByTimeStampAfter(LocalDateTime.now().minusDays(1));

        ReportDto reportDto = reportService.getReportDto(vacancyEntities);

//        DailyReportDto dailyReportDto = new DailyReportDto(vacancyEntities);
//        return dailyReportDto.toString();

        return reportService.getString(reportDto);
    }

    public Optional<VacancyEntity> getByHhId(String hhId) {
        return vacancyEntityRepository.getByHhId(hhId);
    }

    @Loggable
    public List<HhVacancyDto> getUnique(List<HhVacancyDto> vacancyList) {
        return vacancyList.stream().filter(element -> vacancyEntityRepository.getByHhId(element.getId()).isEmpty()).toList();
    }

    public void updateTimeStamp(List<HhVacancyDto> vacancyEntityList) {
        vacancyEntityList.forEach(this::updateVacancyTimeStamp);
    }

    private void updateVacancyTimeStamp(HhVacancyDto element) {
        var vacancyOpt = getByHhId(element.getId());
        vacancyOpt.ifPresent(vacancy -> updateResponses(vacancy, element.getCounters().getTotalResponses()));
    }

    private void updateResponses(VacancyEntity vacancy, int responses) {
        vacancy.setStatus(VacancyStatus.UPDATED);
        vacancy.setResponses(responses);
        save(vacancy);
    }

    public void saveAll(List<HhVacancyDto> vacancyEntityList) {
        for (HhVacancyDto element : vacancyEntityList) {
            VacancyEntity vacancy = getByVacancyDto(element);
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

    public void updateVacancyStatusFromNegotiationsList(HhListDto<HhNegotiationsDto> negotiationsList) {
        List<VacancyEntity> list = getVacancyListFromListDto(negotiationsList);
        list.forEach(element -> {
            Optional<VacancyEntity> entity = getByHhId(element.getHhId());
            updateStatusVacancyEntity(entity, element.getStatus());
        });
    }

    public void updateStatusVacancyEntity(Optional<VacancyEntity> entity, VacancyStatus status) {
        if (entity.isPresent()) {
            VacancyEntity vacancyExt = entity.get();
            if (!vacancyExt.getStatus().equals(status)) {
                vacancyExt.setStatus(status);
                save(vacancyExt);
            }
        }

    }

    public Optional<VacancyEntity> getById(String id) {
        return vacancyEntityRepository.getByHhId(id);
    }

    public void save(VacancyEntity vacancy) {
        vacancy.setTimeStamp(LocalDateTime.now());
        vacancyEntityRepository.save(vacancy);
    }

    public void changeAllStatus(List<HhVacancyDto> list, VacancyStatus status) {
        list.forEach(element -> changeVacancyStatus(element, status));
    }

    private void changeVacancyStatus(HhVacancyDto element, VacancyStatus status) {
        var vacancyOpt = getByHhId(element.getId());
        vacancyOpt.ifPresent(vacancy -> {
            vacancy.setStatus(status);
            save(vacancy);
        });
    }

    public List<VacancyEntity> getAll() {
        return vacancyEntityRepository.findAll();
    }
}
