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
import ru.bereshs.hhworksearch.model.MessageEntity;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhResumeDto;
import ru.bereshs.hhworksearch.service.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResumeController.class)
class ResumeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private HhService service;
    @MockBean
    private KeyEntityService keyEntityService;
    @MockBean
    private SkillsEntityService skillsEntityService;
    @MockBean
    private MessageEntityService negotiationsService;
    @MockBean
    private ResumeEntityService resumeEntityService;

    @Test
    void postSkillSetTest() throws Exception {
        List<SkillEntity> skills = new ArrayList<>();
        SkillEntity skillEntity = getSkillEntity();
        skills.add(skillEntity);

        mockMvc.perform(post("/api/resume/skill_set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJson(skills)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("Body = ok")));
    }

    @Test
    void patchSkillEntityByIdTest() throws Exception {
        SkillEntity skillEntity = getSkillEntity();
        mockMvc.perform(patch("/api/resume/skill_set/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJson(skillEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("Body = ok")));
    }

    @Test
    void getSkillsSetTest() throws Exception {
        SkillEntity skillEntity = getSkillEntity();
        List<SkillEntity> skillEntityList = new ArrayList<>();
        skillEntityList.add(skillEntity);
        HhListDto<SkillEntity> skillsHh = new HhListDto<>(skillEntityList);

        Mockito.when(skillsEntityService.getAll()).thenReturn(skillsHh);
        mockMvc.perform(get("/api/resume/skill_set"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("Test skillEntity")));
    }

    @Test
    void postMessageTest() throws Exception {

        MessageEntity message = getMessage();
        mockMvc.perform(post("/api/resume/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJson(message)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("ok")));
    }

    @Test
    void patchMessageByIdTest() throws Exception {
        MessageEntity message = getMessage();

        mockMvc.perform(patch("/api/resume/message/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJson(message)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("ok")));
    }


    @Test
    void updateResumeTest() throws Exception {
        Mockito.when(service.updateResume(Mockito.any())).thenReturn(getResumeDto());
        mockMvc.perform(post("/api/resume/9999/publish"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("ok")));
    }



    @Test
    void getMineResumesTest() throws Exception {
        var resumeDto =  getResumeDto();
        List<HhResumeDto> resumeDtos = new ArrayList<>();
        resumeDtos.add(resumeDto);
        var result = new HhListDto<>(resumeDtos);
        Mockito.when(service.getActiveResumes()).thenReturn(result);
        mockMvc.perform(get("/api/resume/mine"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("vasiliy")));
    }

    @Test
    void setDefaultResumeTest() throws Exception {
        HhResumeDto resumeDto = getResumeDto();
        mockMvc.perform(post("/api/resume/default")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJson(resumeDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything("ok")));
    }

    @Test
    void getResumeByIdTest() throws Exception {
        var resume = getResumeDto();
        String resumeId="77777";
        Mockito.when(service.getResumeById(Mockito.any(String.class))).thenReturn(resume);

        mockMvc.perform(get("/api/resume/"+resumeId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.anything(resume.getTitle())));

    }

    public byte[] getJson(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o).getBytes();
    }

    public SkillEntity getSkillEntity() {
        SkillEntity skillEntity = new SkillEntity();
        skillEntity.setName("Test skillEntity");
        skillEntity.setDescription("has smallest information");
        return skillEntity;
    }

    public MessageEntity getMessage() {
        MessageEntity message = new MessageEntity();
        message.setFooter("some last text");
        message.setHeader("some first text");
        return message;
    }

    public HhResumeDto getResumeDto() {
        HhResumeDto resumeDto = new HhResumeDto();
        resumeDto.setSkills("some-skill");
        resumeDto.setFirstName("vasiliy");
        resumeDto.setTitle("super-vasiliy");

        return resumeDto;
    }
}