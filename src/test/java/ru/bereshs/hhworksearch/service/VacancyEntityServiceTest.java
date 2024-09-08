package ru.bereshs.hhworksearch.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bereshs.hhworksearch.mapper.AppMapper;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhNegotiationsDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.model.dto.ReportDto;
import ru.bereshs.hhworksearch.repository.VacancyEntityRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class VacancyEntityServiceTest {

    @Mock
    private VacancyEntityRepository vacancyEntityRepository;

    @InjectMocks
    private VacancyEntityService vacancyEntityService;

    @Mock
    private AppMapper mapper;

    @Mock
    private DailyReportService reportService;

    @Test
    void getDaily() {
        String expected = "Ежедневный отчет:\n" +
                "\tвсего записей 1\n" +
                "\tотправлено запросов 1\n" +
                "\tприглашений 0\n" +
                "\tотказов 0\n" +
                "\tне подошло 0\n" +
                "\tсредняя зарплата:";
        Mockito.when(vacancyEntityRepository.getVacancyEntitiesByTimeStampAfter(Mockito.any())).thenReturn(List.of(getVacancyEntity()));
        Mockito.when(reportService.getReportDto(Mockito.any())).thenReturn(new ReportDto(
                1L,
                1L,
                1L,
                1L,
                1L,
                "salary"
        ));
        Mockito.when(reportService.getString(Mockito.any())).thenReturn(expected);
        String actual = vacancyEntityService.getDaily();
        assertTrue(actual.contains(expected));
    }


    @Test
    void saveAll() {
        HhVacancyDto vacancy = getVacancyDto();
        List<HhVacancyDto> list = new ArrayList<>();
        list.add(vacancy);
        list.add(vacancy);
        Mockito.when(mapper.toVacancyEntity(Mockito.any())).thenReturn(getVacancyEntity());
        vacancyEntityService.saveAll(list);
        Mockito.verify(vacancyEntityRepository, Mockito.times(2)).save(Mockito.any(VacancyEntity.class));
    }


    @Test
    void getByVacancyDto() {
        HhVacancyDto vacancyDto = getVacancyDto();
        VacancyEntity vacancy = getVacancyEntity();
        Mockito.when(vacancyEntityRepository.getByHhId(Mockito.any())).thenReturn(Optional.ofNullable(vacancy));
        VacancyEntity entity = vacancyEntityService.getByVacancyDto(vacancyDto);
        assertEquals(entity.getDescription(), vacancyDto.getDescription());
    }

    @Test
    void updateStatusVacancyStatusFromNegotiationList() {
        Optional<VacancyEntity> vacancy = Optional.of(getVacancyEntity());
        HhListDto<HhNegotiationsDto> negotiations = new HhListDto<>(List.of(getHhNegotiationsDto()));
        Mockito.when(vacancyEntityRepository.getByHhId(Mockito.any())).thenReturn(Optional.of(getVacancyEntity()));
        Mockito.when(mapper.toVacancyEntity(Mockito.any())).thenReturn(getVacancyEntity());
        vacancyEntityService.updateVacancyStatusFromNegotiationsList(negotiations);
        Mockito.verify(vacancyEntityRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void changeVacancyStatus() {
        Mockito.when(vacancyEntityRepository.getByHhId(Mockito.any())).thenReturn(Optional.of(getVacancyEntity()));
        List<HhVacancyDto> list = List.of(getVacancyDto());
        vacancyEntityService.changeAllStatus(list, VacancyStatus.UPDATED);
        Mockito.verify(vacancyEntityRepository, Mockito.times(1)).save(Mockito.any());
    }

    HhVacancyDto getVacancyDto() {
        HhSimpleListDto emploer = new HhSimpleListDto();
        emploer.setName("empl");
        emploer.setId("234");
        HhVacancyDto vacancyDto = new HhVacancyDto();
        vacancyDto.setName("vacancy");
        vacancyDto.setDescription("some information");
        vacancyDto.setCreatedAt(LocalDateTime.now());
        vacancyDto.setPublishedAt("2024-07-03T09:00:03+0300");
        vacancyDto.setEmployer(emploer);
        HhSimpleListDto experience = new HhSimpleListDto();
        experience.setId("between1And3");
        vacancyDto.setExperience(experience);
        return vacancyDto;
    }

    VacancyEntity getVacancyEntity() {
        var vacancy = new VacancyEntity();
        HhVacancyDto vacancyDto = getVacancyDto();
        vacancy.setDescription(vacancyDto.getDescription());
        vacancy.setName(vacancyDto.getName());
        vacancy.setCreatedAt(vacancyDto.getCreatedAt());
        vacancy.setPublished(vacancyDto.getCreatedAt());
        vacancy.setExperience(vacancyDto.getExperience());
        vacancy.setStatus(VacancyStatus.REQUEST);
        return vacancy;
    }

    HhNegotiationsDto getHhNegotiationsDto() {
        HhNegotiationsDto hhNegotiationsDto = new HhNegotiationsDto();
        hhNegotiationsDto.setId("123");
        hhNegotiationsDto.setVacancy(getVacancyDto());
        HhSimpleListDto state = new HhSimpleListDto();
        state.setId("found");
        hhNegotiationsDto.setState(state);
        hhNegotiationsDto.setCreatedAt("2024-07-03T09:00:03+0300");
        hhNegotiationsDto.setUpdatedAt("2024-07-03T09:00:03+0300");

        return hhNegotiationsDto;
    }
}