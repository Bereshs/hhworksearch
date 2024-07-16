package ru.bereshs.hhworksearch.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.producer.KafkaProducer;
import ru.bereshs.hhworksearch.service.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VacancyController.class)
class VacancyControllerTest {

    @MockBean
    private KeyEntityService keyEntityService;
    @MockBean
    private SkillsEntityService skillsEntityService;
    @MockBean
    private MessageEntityService negotiationsService;
    @MockBean
    private VacancyEntityService vacancyEntityService;
    @MockBean
    private FilterEntityService filterEntityService;
    @MockBean
    private HhService hhService;
    @MockBean
    private EmployerEntityService employerEntityService;

    @MockBean
    private ResumeEntityService resumeEntityService;

    @MockBean
    private KafkaProducer producer;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRecommendedVacancyListTest() throws Exception {

        var vacancyDto = getVacancyDto();
        List<HhVacancyDto> list = new ArrayList<>();
        list.add(vacancyDto);
        Mockito.when(vacancyEntityService.getUnique(Mockito.anyList())).thenReturn(list);
        mockMvc.perform(get("/api/vacancy/recommended"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("some information")));
    }

    @Test
    void viewVacancyPageTest() throws Exception {
        Mockito.when(hhService.getVacancyById(Mockito.any())).thenReturn(getVacancyDto());

        mockMvc.perform(get("/api/vacancy/123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("some information")));

    }

    @Test
    void postNegotiationTest() throws Exception {
        Mockito.when(vacancyEntityService.getById(Mockito.any())).thenReturn(Optional.of(getVacancyEntity()));
        Mockito.when(hhService.getVacancyById(Mockito.any())).thenReturn(getVacancyDto());
        Mockito.when(resumeEntityService.getRelevantResume(Mockito.any())).thenReturn(getResume());
        mockMvc.perform(post("/api/vacancy/9999/resume/99999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("beer")));
    }

    @Test
    void postNegotiationTrowTest() throws Exception {
        VacancyEntity vacancy = getVacancyEntity();
        vacancy.setStatus(VacancyStatus.REQUEST);
        Mockito.when(vacancyEntityService.getById(Mockito.any())).thenReturn(Optional.of(vacancy));
        mockMvc.perform(post("/api/vacancy/9999/resume/99999"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void postNegotiationWrongIdTest() throws Exception {
        Mockito.when(vacancyEntityService.getById(Mockito.any())).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/vacancy/9999/resume/99999"))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    void getVacancyMessageTest() throws Exception {
        Mockito.when(hhService.getVacancyById(Mockito.any())).thenReturn(getVacancyDto());
        Mockito.when(negotiationsService.getMessageById(Mockito.anyLong())).thenReturn(getMessage());

        SkillEntity skillEntity = new SkillEntity("beer");
        List<SkillEntity> skillEntityList = new ArrayList<>();
        skillEntityList.add(skillEntity);

        Mockito.when(skillsEntityService.extractVacancySkills(Mockito.any())).thenReturn(skillEntityList);

        mockMvc.perform(get("/api/vacancy/77777/message"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("some header")));
    }

    MessageEntity getMessage() {
        MessageEntity message = new MessageEntity();
        message.setHeader("some header");
        message.setFooter("some footer");
        return message;
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

    VacancyEntity getVacancyEntity() {
        var vacancy = new VacancyEntity();
        HhVacancyDto vacancyDto = getVacancyDto();
        vacancy.setDescription(vacancyDto.getDescription());
        vacancy.setName(vacancyDto.getName());
        vacancy.setCreatedAt(vacancyDto.getCreatedAt());
        vacancy.setPublished(vacancyDto.getCreatedAt());
        vacancy.setStatus(VacancyStatus.FOUND);
        return vacancy;
    }

    ResumeEntity getResume() {
        var resume  =  new ResumeEntity();
        resume.setHhId("292992");
        return resume;
    }

}
