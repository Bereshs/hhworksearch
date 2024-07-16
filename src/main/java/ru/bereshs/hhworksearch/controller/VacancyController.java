package ru.bereshs.hhworksearch.controller;

import com.github.scribejava.core.model.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.producer.KafkaProducer;
import ru.bereshs.hhworksearch.service.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@Tag(name = "Вакансии",
        description = "Работа с вакансиями")
public class VacancyController {

    private final SkillsEntityService skillsEntityService;
    private final MessageEntityService messageEntityService;
    private final VacancyEntityService vacancyEntityService;
    private final ResumeEntityService resumeEntityService;
    private final FilterEntityService<HhVacancyDto> filterEntityService;
    private final HhService service;
    private final EmployerEntityService employerEntityService;
    private final KafkaProducer producer;

    @Operation(summary = "Рекомендованные мне вакансии")
    @GetMapping("/api/vacancy/recommended")
    public List<HhVacancyDto> getRecommendedVacancyList() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        var vacancyList = getVacancyEntityList();
        var filteredList = filterEntityService.doFilterNameAndExperience(vacancyList);
        return saveUniqueList(filteredList);
    }


    @Operation(summary = "Просмотр вакансии")
    @GetMapping("/api/vacancy/{id}")
    public HhVacancyDto viewVacancyPage(@PathVariable String id) throws  IOException, ExecutionException, InterruptedException {
        return service.getVacancyById(id);
    }

    @Operation(summary = "Отправка отклика на вакансию")
    @PostMapping("/api/vacancy/{vacancyId}/resume/{resumeId}")
    public String postNegotiation(@PathVariable String vacancyId, @PathVariable String resumeId) throws HhWorkSearchException, IOException, ExecutionException, InterruptedException {
        VacancyEntity vacancyEntity = vacancyEntityService.getById(vacancyId).orElseThrow(() -> new HhWorkSearchException("Wrong vacancyId"));
        if (!vacancyEntity.isNotRequest()) {
            throw new HhWorkSearchException("Negotiation on vacancy already requested");
        }
        HhVacancyDto vacancy = service.getVacancyById(vacancyId);
        ResumeEntity resume = resumeEntityService.getRelevantResume(vacancy);

        List<SkillEntity> skills = skillsEntityService.extractVacancySkills(vacancy);

        var body = messageEntityService.getNegotiationBody(skills, resume.getHhId(), vacancy);
        Response response = service.postNegotiation(body);
        producer.produce(response, vacancy.getId());

        return "ok";
    }

    @Operation(summary = "Сопроводительное письмо для вакансии")
    @GetMapping("/api/vacancy/{id}/message")
    public String getVacancyMessage(@PathVariable String id) throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        HhVacancyDto vacancyDto = service.getVacancyById(id);

        MessageEntity message = messageEntityService.getMessageById(1);
        List<SkillEntity> skills = skillsEntityService.extractVacancySkills(vacancyDto);
        return message.getMessage(
                skillsEntityService.updateList(skills),
                vacancyDto.getName()
        );
    }



    private List<HhVacancyDto> getVacancyEntityList() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        String key = filterEntityService.getKey();
        var list = service.getRecommendedVacancy(key);
        var employerList = employerEntityService.extractEmployers(list);
        employerEntityService.saveAll(employerList);
        return list;
    }

    private List<HhVacancyDto> saveUniqueList(List<HhVacancyDto> list) {
        var unique = vacancyEntityService.getUnique(list);
        vacancyEntityService.saveAll(unique);
        return unique;
    }


}
