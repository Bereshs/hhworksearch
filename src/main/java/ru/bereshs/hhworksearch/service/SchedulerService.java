package ru.bereshs.hhworksearch.service;

import com.github.scribejava.core.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhResumeDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.producer.KafkaProducer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final VacancyEntityService vacancyEntityService;
    private final HhService service;
    private final FilterEntityService<HhVacancyDto> filterEntityService;
    private final KafkaProducer producer;
    private final ResumeEntityService resumeEntityService;
    private final MessageEntityService messageEntityService;
    private final EmployerEntityService employerEntityService;
    private final SkillsEntityService skillsEntityService;

    @Loggable
    public void dailyLightTaskRequest() throws InterruptedException, IOException, ExecutionException, HhWorkSearchException {

        List<HhVacancyDto> vacancyList = getPageRecommendedVacancy(getKey());
        postNegotiationWithRelevantVacancies(vacancyList);
        updateResume();
    }

    @Loggable
    public void dailyFullRequest() throws InterruptedException, IOException, ExecutionException, HhWorkSearchException {
        List<HhVacancyDto> vacancyList = getFullHhVacancy();
        postNegotiationWithRelevantVacancies(vacancyList);
        updateVacancyStatus();
        sendMessageDailyReport();
    }

    @Loggable
    public void dailyRecommendedRequest() throws InterruptedException, IOException, ExecutionException, HhWorkSearchException {
        List<HhVacancyDto> vacancyList = service.getPageRecommendedVacancyForResume(resumeEntityService.getDefault()).getItems();
        postNegotiationWithRelevantVacancies(vacancyList);
    }


    public void updateVacancyStatus() throws InterruptedException, IOException, ExecutionException {
        var negotiationsList = service.getHhNegotiationsDtoList();
        vacancyEntityService.updateVacancyStatusFromNegotiationsList(negotiationsList);
    }

    private void sendMessageDailyReport() throws HhWorkSearchException {
        String message = vacancyEntityService.getDaily();
        producer.produceDefault(message);
    }

    private void postNegotiationWithRelevantVacancies(List<HhVacancyDto> vacancyList) throws InterruptedException, IOException, ExecutionException, HhWorkSearchException {
        if (vacancyList.isEmpty()) return;
        var filtered = getRelevantVacancies(vacancyList);
        vacancyEntityService.saveAll(filtered);
        postNegotiations(filtered);
        vacancyEntityService.changeAllStatus(filtered, VacancyStatus.REQUEST);
    }


    private void updateResume() throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        ResumeEntity resume = resumeEntityService.getDefault();
        HhResumeDto resumeDto = service.updateResume(resume);
        resumeEntityService.setNextPublish(resume, resumeDto.getNextPublishAt());

    }

    private void postNegotiations(List<HhVacancyDto> filtered) throws InterruptedException, IOException, ExecutionException, HhWorkSearchException {
        for (HhVacancyDto element : filtered) {
            var vacancyEntity = vacancyEntityService.getById(element.getId());
            if (vacancyEntity.isPresent() && !vacancyEntity.get().isNotRequest()) {
                return;
            }
            HhVacancyDto vacancy = service.getVacancyById(element.getId());

            ResumeEntity resume = resumeEntityService.getRelevantResume(vacancy);
            List<SkillEntity> skills = skillsEntityService.extractVacancySkills(vacancy);

            var body = messageEntityService.getNegotiationBody(skills, resume.getHhId(), vacancy);
            Response response = service.postNegotiation(body);
            producer.produce(response, vacancy.getId());


            //      negotiationsService.postNegotiationForVacancy(vacancy);
        }
    }

    @Loggable
    private List<HhVacancyDto> getRelevantVacancies(List<HhVacancyDto> vacancyList) throws InterruptedException, IOException, ExecutionException {
        List<HhVacancyDto> filtered = filterEntityService.doFilterNameAndExperience(vacancyList);
        List<HhVacancyDto> list = vacancyEntityService.getUnique(filtered);
        var full = service.getFullVacancyInformation(list);
        vacancyEntityService.updateTimeStamp(full);
        filtered = filterEntityService.doFilterDescription(full);
        return filtered;
    }


    private List<HhVacancyDto> getFullHhVacancy() throws InterruptedException {
        return getRecommendedVacancy();

    }


    @Loggable
    private List<HhVacancyDto> getRecommendedVacancy() throws InterruptedException {
        try {
            var list = service.getRecommendedVacancy(getKey());
            var listEmployer = employerEntityService.extractEmployers(list);
            employerEntityService.saveAll(listEmployer);
            return list;
        } catch (IOException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<HhVacancyDto> getPageRecommendedVacancy(String key) throws InterruptedException {
        try {
            return service.getPageRecommendedVacancy(0, key).getItems();
        } catch (IOException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String getKey() {
        return filterEntityService.getKey();
    }
}
