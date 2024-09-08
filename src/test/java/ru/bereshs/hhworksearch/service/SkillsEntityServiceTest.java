package ru.bereshs.hhworksearch.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import ru.bereshs.hhworksearch.config.KafkaProducerConfig;
import ru.bereshs.hhworksearch.config.SchedulerConfig;
import ru.bereshs.hhworksearch.controller.web.AuthorizationController;
import ru.bereshs.hhworksearch.controller.ManagementController;
import ru.bereshs.hhworksearch.hhapiclient.impl.HeadHunterClientRestTemplate;
import ru.bereshs.hhworksearch.producer.KafkaProducerImpl;
import ru.bereshs.hhworksearch.repository.SkillsEntityRepository;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("Test")
class SkillsEntityServiceTest {
    @Mock
    KafkaProducerConfig kafkaProducerConfig;
    @Mock
    SchedulerConfig schedulerConfig;
    @Mock
    AuthorizationController authorizationController;
    @Mock
    KeyEntityService keyEntityService;
    @Mock
    HeadHunterClientRestTemplate headHunterClient;
    @Mock
    KafkaProducerImpl kafkaProducer;
    @Mock
    ManagementController managementController;


    @InjectMocks
    SkillsEntityService skillsEntityService;

    @Mock
    SkillsEntityRepository skillsEntityRepository;

    @Test
    void extractVacancySkillsTest() {
        List<SkillEntity> skills = new ArrayList<>();

        skills.add(new SkillEntity("java"));
        skills.add(new SkillEntity("maven"));
        skills.add(new SkillEntity("git"));
        skills.add(new SkillEntity("английский"));

        HhVacancyDto vacancy = new HhVacancyDto();
        vacancy.setName("Java разработчик");
        vacancy.setDescription("Нам важно занание maven");
        List<HhSimpleListDto> vacancySkills = new ArrayList<>();
        HhSimpleListDto vacancySkillsDto = new HhSimpleListDto();
        vacancySkillsDto.setName("git");
        vacancySkills.add(vacancySkillsDto);

        vacancy.setSkills(vacancySkills);
        Mockito.when(skillsEntityRepository.findAll()).thenReturn(skills);
        List<SkillEntity> calculateSkills = skillsEntityService.extractVacancySkills(vacancy);

        assertTrue(calculateSkills.toString().contains("java"));
        assertTrue(calculateSkills.toString().contains("git"));
        assertTrue(calculateSkills.toString().contains("maven"));
        assertFalse(calculateSkills.toString().contains("английский"));
    }


}