package ru.bereshs.hhworksearch.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.bereshs.hhworksearch.config.AppConfig;
import ru.bereshs.hhworksearch.controller.web.AuthorizationController;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhListDto;
import ru.bereshs.hhworksearch.service.KeyEntityService;
import ru.bereshs.hhworksearch.service.HhService;
import ru.bereshs.hhworksearch.service.ResumeEntityService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorizationController.class)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HhService service;
    @MockBean
    private KeyEntityService keyEntityService;
    @MockBean
    private AppConfig appConfig;

    @MockBean
    private ResumeEntityService resumeEntityService;

    @Test
    void authorizationPageTest() throws Exception {
        Mockito.when(service.getPageRecommendedVacancyForResume(Mockito.any())).thenReturn(new HhListDto<>());
        Mockito.when(service.getActiveResumes()).thenReturn(new HhListDto<>());
        Mockito.when(service.getAccessToken(Mockito.any())).thenReturn(getToken());

        mockMvc.perform(get("/authorization?code=777")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("Данные пользователя")));
    }

    @Test
    void authorizationNoCodePageTest() throws Exception {
        //      Mockito.when(service.getMePageBody()).thenReturn("someString");
        Mockito.when(service.getPageRecommendedVacancyForResume(Mockito.any())).thenReturn(new HhListDto<>());
        Mockito.when(service.getActiveResumes()).thenReturn(new HhListDto<>());

        mockMvc.perform(get("/authorization"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("Received error")));
    }

    @Test
    void authoredPageTest() throws Exception {
//        Mockito.when(service.getMePageBody()).thenReturn(new HashMap<String, capture of ?>());
        Mockito.when(service.getPageRecommendedVacancyForResume(Mockito.any())).thenReturn(new HhListDto<>());

        Mockito.when(service.getActiveResumes()).thenReturn(new HhListDto<>());
        Mockito.when(service.getToken()).thenReturn(getToken());
        mockMvc.perform(get("/authorized"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("Данные пользователя")));
    }

    OAuth2AccessToken getToken() {
        return new OAuth2AccessToken(
                "accessToken",
                "tokenType",
                120300,
                "refreshToken",
                "scope",
                "rawResponse");
    }

}