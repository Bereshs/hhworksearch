package ru.bereshs.hhworksearch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bereshs.hhworksearch.model.MessageEntity;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.*;
import ru.bereshs.hhworksearch.service.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@Slf4j
@Tag(name = "Резюме",
        description = "Работа с резюме")
public class ResumeController {

    private final HhService service;
    private final SkillsEntityService skillsEntityService;
    private final MessageEntityService messageEntityService;
    private final ResumeEntityService resumeEntityService;


    @Operation(summary = "Отправка списка скилов")
    @PostMapping("/api/resume/skill_set")
    public String postSkillSet(@RequestBody List<SkillEntity> skills) {
        skillsEntityService.saveAll(skills);
        return "ok";
    }

    @Operation(summary = "Изменение скила")
    @PatchMapping("/api/resume/skill_set/{id}")
    public String patchSkillEntityById(@PathVariable long id, @RequestBody SkillEntity entityDto) throws HhWorkSearchException {
        skillsEntityService.patchSkillEntityById(id, entityDto);
        return "ok";
    }

    @Operation(summary = "Получение списка скилов")
    @GetMapping("/api/resume/skill_set")
    public HhListDto<SkillEntity> getSkillsSet() {
        return skillsEntityService.getAll();
    }


    @Operation(summary = "Добавления сопроводительного письма")
    @PostMapping("/api/resume/message")
    public String postMessage(@RequestBody MessageEntity messageDto) {
        messageEntityService.save(messageDto);
        return "ok";
    }

    @Operation(summary = "Изменение сопроводительного письма")
    @PatchMapping("/api/resume/message/{id}")
    public String patchMessageById(@RequestBody MessageEntity messageDto, @PathVariable long id) throws HhWorkSearchException {
        messageEntityService.patchMessageById(id, messageDto);
        return "ok";
    }


    @Operation(summary = "Обновление резюме по Id")
    @PostMapping("/api/resume/{id}/publish")
    public String updateResume(@PathVariable String id) throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        ResumeEntity resume = resumeEntityService.getByHhid(id);
        HhResumeDto resumeDto  = service.updateResume(resume);
        resumeEntityService.setNextPublish(resume, resumeDto.getNextPublishAt());
        return "ok";
    }

    @Operation(summary = "Просмотры моего резюме")
    @GetMapping("/api/resume/views")
    public HhListDto<HhViewsResume> getViewsResumeList() throws IOException, ExecutionException, InterruptedException {
        ResumeEntity resume = resumeEntityService.getDefault();
        return new HhListDto<>(service.getHhViewsResumeDtoList(resume.getHhId()).getItems());
    }

    @Operation(summary = "Мои резюме с hh.ru")
    @GetMapping("/api/resume/mine")
    public HhListDto<HhResumeDto> getMineResumes() throws IOException, ExecutionException, InterruptedException {
        return service.getActiveResumes();
    }


    @Operation(summary = "Установка резюме по умолчанию")
    @PostMapping("/api/resume/default")
    public String setDefaultResume(@RequestBody HhResumeDto resumeDto) {
        ResumeEntity resume = resumeEntityService.getByHhid(resumeDto.getId());
        resumeEntityService.setDefault(resume);
        return "ok";
    }

    @Operation(summary = "Просмотр резюме")
    @GetMapping("/api/resume/{resumeId}")
    public HhResumeDto getResumeById(@PathVariable String resumeId) throws IOException, ExecutionException, InterruptedException {
        return service.getResumeById(resumeId);
    }

}
