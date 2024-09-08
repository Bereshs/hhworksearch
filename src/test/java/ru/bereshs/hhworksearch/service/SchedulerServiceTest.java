package ru.bereshs.hhworksearch.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.*;
import ru.bereshs.hhworksearch.producer.KafkaProducer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @Mock
    private HhService service;
    @Mock
    private SettingsService settingsService;
    @Mock
    private VacancyEntityService vacancyEntityService;
    @Mock
    private FilterEntityService filterEntityService;
    @Mock
    private KeyEntityService keyEntityService;
    @Mock
    private ResumeEntityService resumeEntityService;
    @InjectMocks
    private SchedulerService schedulerService;
    @Mock
    private EmployerEntityService employerEntityService;

    @Mock
    private KafkaProducer producer;

    @Test
    void dailyLightTaskRequest() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        Mockito.when(settingsService.isDemonActive()).thenReturn(true);
        Mockito.when(service.getPageRecommendedVacancy(Mockito.anyInt(), Mockito.any())).thenReturn(getHhVacancyDtoList());
        Mockito.when(service.updateResume(Mockito.any())).thenReturn(getResumeDto());
        schedulerService.dailyLightTaskRequest();
        Mockito.verify(vacancyEntityService, Mockito.times(1)).saveAll(Mockito.any());
    }

    @Test
    void dailyFullRequest() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        Mockito.when(settingsService.isDemonActive()).thenReturn(true);
        Mockito.when(service.getHhNegotiationsDtoList()).thenReturn(getNegotiationsList());
        schedulerService.dailyFullRequest();
        Mockito.verify(vacancyEntityService, Mockito.times(1)).updateVacancyStatusFromNegotiationsList(Mockito.any());
    }

    @Test
    void dailyRecommendedRequest() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        Mockito.when(settingsService.isDemonActive()).thenReturn(true);
        Mockito.when(service.getPageRecommendedVacancyForResume(Mockito.any())).thenReturn(getHhVacancyDtoList());
        schedulerService.dailyRecommendedRequest();
        Mockito.verify(vacancyEntityService, Mockito.times(1)).saveAll(Mockito.any());
    }

    @Test
    void updateVacancyStatus() throws IOException, ExecutionException, InterruptedException {
        Mockito.when(service.getHhNegotiationsDtoList()).thenReturn(getNegotiationsList());
        schedulerService.updateVacancyStatus();
        Mockito.verify(vacancyEntityService, Mockito.times(1)).updateVacancyStatusFromNegotiationsList(Mockito.any());

    }

    HhListDto<HhVacancyDto> getHhVacancyDtoList() {
        return new HhListDto<>(List.of(getVacancyDto()));
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
        return vacancyDto;
    }

    HhResumeDto getResumeDto() {
        HhResumeDto resumeDto=  new HhResumeDto();
        resumeDto.setNextPublishAt("2024-07-03T09:00:03+0300");
        return resumeDto;
    }
    HhListDto<HhNegotiationsDto> getNegotiationsList() {
        HhNegotiationsDto negotiationsDto = getNegotiationsDto();
        return new HhListDto<>(List.of(negotiationsDto));
    }

    HhNegotiationsDto getNegotiationsDto() {
        HhNegotiationsDto negotiationsDto = new HhNegotiationsDto();
        negotiationsDto.setVacancy(new HhVacancyDto());
        return negotiationsDto;
    }

}