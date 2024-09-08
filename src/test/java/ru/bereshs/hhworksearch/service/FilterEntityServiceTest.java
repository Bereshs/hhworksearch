package ru.bereshs.hhworksearch.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.ActiveProfiles;
import ru.bereshs.hhworksearch.config.KafkaProducerConfig;
import ru.bereshs.hhworksearch.config.SchedulerConfig;
import ru.bereshs.hhworksearch.controller.web.AuthorizationController;
import ru.bereshs.hhworksearch.controller.ManagementController;
import ru.bereshs.hhworksearch.model.FilterScope;
import ru.bereshs.hhworksearch.hhapiclient.impl.HeadHunterClientRestTemplate;
import ru.bereshs.hhworksearch.producer.KafkaProducerImpl;
import ru.bereshs.hhworksearch.repository.FilterEntityRepository;
import ru.bereshs.hhworksearch.model.FilterEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@WebMvcTest(FilterEntityService.class)
@ActiveProfiles("Test")

class FilterEntityServiceTest {
    @MockBean
    KafkaProducerConfig kafkaProducerConfig;
    @MockBean
    SchedulerConfig schedulerConfig;
    @MockBean
    AuthorizationController authorizationController;
    @MockBean
    KeyEntityService keyEntityService;
    @MockBean
    HeadHunterClientRestTemplate headHunterClient;
    @MockBean
    KafkaProducerImpl kafkaProducer;
    @MockBean
    ManagementController managementController;

    @Autowired
    private FilterEntityService filterEntityService;

    @MockBean
    private FilterEntityRepository filterEntityRepository;

    private List<FilterEntity> filterEntityListName;
    private List<FilterEntity> filterEntityListExperience;

    private VacancyEntity vacancy;
    private final FilterScope filterScopeExperienceParameter = FilterScope.EXPERIENCE;
    private static final String BETWEEN_1_AND_3 ="between1and3";
    private final FilterScope filterScopeNameParameter = FilterScope.NAME;

    @BeforeEach
    void setup() {
        filterEntityListName = new ArrayList<>();
        FilterEntity filterEntity = new FilterEntity();
        filterEntity.setScope(filterScopeNameParameter);
        filterEntity.setWord("kotlin");
        filterEntityListName.add(filterEntity);

        filterEntityListExperience = new ArrayList<>();
        filterEntity = new FilterEntity();
        filterEntity.setScope(filterScopeExperienceParameter);
        filterEntity.setWord(BETWEEN_1_AND_3);
        filterEntityListExperience.add(filterEntity);

        vacancy = new VacancyEntity();
        vacancy.setName("Kotlin разработчик");
        vacancy.setExperience(BETWEEN_1_AND_3);

    }

    @Test
    void doFilterTest() {
        List<VacancyEntity> vacancyEntities = new ArrayList<>();
        VacancyEntity vacancyEntity = new VacancyEntity();
        vacancyEntity.setName("Java разработчик");
        vacancyEntity.setExperience(BETWEEN_1_AND_3);
        vacancyEntities.add(vacancyEntity);
        vacancyEntity = new VacancyEntity();
        vacancyEntity.setName("разработчик");
        vacancyEntity.setExperience("between3and6");
        vacancyEntities.add(vacancyEntity);
        vacancyEntities.add(vacancy);

        Mockito.when(filterEntityRepository.findFilterEntityByScope(filterScopeNameParameter))
                .thenReturn(filterEntityListName);
        Mockito.when(filterEntityRepository.findFilterEntityByScope(filterScopeExperienceParameter))
                .thenReturn(filterEntityListExperience);

        List<VacancyEntity> filteredList = filterEntityService.doFilterNameAndExperience(vacancyEntities);

        assertEquals(1, filteredList.size());
        assertEquals("разработчик", filteredList.get(0).getName());


    }

    @Test
    void isValidTest() {
        Mockito.when(filterEntityRepository.findFilterEntityByScope(filterScopeNameParameter))
                .thenReturn(filterEntityListName);
        Mockito.when(filterEntityRepository.findFilterEntityByScope(filterScopeExperienceParameter))
                .thenReturn(filterEntityListExperience);

        assertFalse(filterEntityService.isValid(vacancy));

    }

    @Test
    void containsExcludeWordsTest() {

        Mockito.when(filterEntityRepository.findFilterEntityByScope(filterScopeNameParameter))
                .thenReturn(filterEntityListName);
        Mockito.when(filterEntityRepository.findFilterEntityByScope(filterScopeExperienceParameter))
                .thenReturn(filterEntityListExperience);

        assertTrue(filterEntityService.isContainWordsScope(vacancy.getName(), filterEntityRepository.findFilterEntityByScope(filterScopeNameParameter)));
        assertTrue(filterEntityService.isContainWordsScope(vacancy.getExperience(), filterEntityRepository.findFilterEntityByScope(filterScopeExperienceParameter)));

    }
}