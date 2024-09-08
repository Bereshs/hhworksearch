package ru.bereshs.hhworksearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.bereshs.hhworksearch.mapper.AppMapper;
import ru.bereshs.hhworksearch.model.SettingsEntity;
import ru.bereshs.hhworksearch.model.dto.SettingsDto;
import ru.bereshs.hhworksearch.service.SettingsService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettingsRestController.class)
class SettingsControllerTest {

    @MockBean
    private SettingsService service;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private AppMapper mapper;

    @Test
    void setParameterTest() throws Exception {
        mockMvc.perform(post("/api/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJson(getSettingsDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("ok")));
    }

    @Test
    void getSettingsTest() throws Exception {
        Mockito.when(service.getAll()).thenReturn(List.of(getSettings()));
        mockMvc.perform(get("/api/settings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("setname1")));
    }

    @Test
    void isDemonActive() throws Exception {
        Mockito.when(service.isDemonActive()).thenReturn(true);
        mockMvc.perform(get("/api/settings/demon"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("true")));
    }

    SettingsDto getSettingsDto() {
        SettingsDto settingsDto = new SettingsDto();
        settingsDto.setName("setname1");
        settingsDto.setValue("value1");
        return settingsDto;
    }

    SettingsEntity getSettings() {
        SettingsDto settingsDto = getSettingsDto();
        SettingsEntity settings =  new SettingsEntity();
        settings.setName(settingsDto.getName());
        settings.setValue(settingsDto.getValue());
        return settings;
    }

    byte[] getJson(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o).getBytes();
    }
}