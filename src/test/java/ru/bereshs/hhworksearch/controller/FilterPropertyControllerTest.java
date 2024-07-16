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
import ru.bereshs.hhworksearch.model.FilterEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.dto.FilterDto;
import ru.bereshs.hhworksearch.service.FilterEntityService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(FilterPropertyController.class)
class FilterPropertyControllerTest {
    @MockBean
    private FilterEntityService<VacancyEntity> filterEntityService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllFilter() throws Exception {
        List<FilterEntity> list = new ArrayList<>();
        list.add(getFilterEntity());

        Mockito.when(filterEntityService.getAll()).thenReturn(list);
        mockMvc.perform(get("/api/filter"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("user")));
    }

    @Test
    void addFilter() throws Exception {
        mockMvc.perform(post("/api/filter").contentType(MediaType.APPLICATION_JSON).content(getJson(getFilterEntity())))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void removeFilter() throws Exception {
        mockMvc.perform(delete("/api/filter").contentType(MediaType.APPLICATION_JSON).content(getJson(getFilterDto())))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

    }

    byte[] getJson(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o).getBytes();
    }

    FilterDto getFilterDto() {
        List<String> wordsList =  new ArrayList<>();
        wordsList.add("user");
        FilterDto filterDto = new FilterDto();
        filterDto.setScope("name");
        filterDto.setWords(wordsList);
        return filterDto;
    }
    FilterEntity getFilterEntity() {
        FilterEntity filterEntity = new FilterEntity();
        filterEntity.setScope("name");
        filterEntity.setId(1);
        filterEntity.setWord("user");
        return filterEntity;
    }
}