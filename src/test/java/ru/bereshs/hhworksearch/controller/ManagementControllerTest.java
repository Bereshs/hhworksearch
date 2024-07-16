package ru.bereshs.hhworksearch.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhNegotiationsDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.producer.KafkaProducer;
import ru.bereshs.hhworksearch.service.KeyEntityService;
import ru.bereshs.hhworksearch.service.HhService;
import ru.bereshs.hhworksearch.service.SchedulerService;
import ru.bereshs.hhworksearch.service.VacancyEntityService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagementController.class)
class ManagementControllerTest {

    @MockBean
    private  HhService service;
    @MockBean
    private KeyEntityService keyEntityService;
    @MockBean
    private  VacancyEntityService vacancyEntityService;
    @MockBean
    private  KafkaProducer kafkaProducer;
    @MockBean
    private  SchedulerService schedulerService;

    @Autowired
    private MockMvc mockMvc;
    @Test
    void getNegotiationsListTest() throws Exception {
        HhListDto<HhNegotiationsDto> list = new HhListDto<>(List.of(getHhNegotiationsDto()));
        Mockito.when(service.getHhNegotiationsDtoList()).thenReturn(list);
        mockMvc.perform(get("/api/negotiations"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateNegotiationsTest() throws Exception {

        mockMvc.perform(post("/api/negotiations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("ok")));
    }

    @Test
    void dailyReportTest() throws Exception {
        Mockito.when(vacancyEntityService.getDaily()).thenReturn("hello");
        mockMvc.perform(get("/api/negotiations/daily"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("hello")));
    }

    @Test
    void hourSchedulerTest() throws Exception {
        mockMvc.perform(get("/api/negotiations/hour"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("ok")));
    }

    @Test
    void dailyRecommendedScheduler() throws Exception {
        mockMvc.perform(get("/api/negotiations/18"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("ok")));
    }

    @Test
    void dailyFullScheduler() throws Exception {
        mockMvc.perform(get("/api/negotiations/19"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("ok")));
    }

    HhNegotiationsDto getHhNegotiationsDto() {
        HhNegotiationsDto hhNegotiationsDto =  new HhNegotiationsDto();
        hhNegotiationsDto.setId("123");
        hhNegotiationsDto.setVacancy(new HhVacancyDto());
        hhNegotiationsDto.setState(new HhSimpleListDto());
        hhNegotiationsDto.setCreatedAt("2024-07-03T09:00:03+0300");
        hhNegotiationsDto.setUpdatedAt("2024-07-03T09:00:03+0300");
        return hhNegotiationsDto;
    }
}