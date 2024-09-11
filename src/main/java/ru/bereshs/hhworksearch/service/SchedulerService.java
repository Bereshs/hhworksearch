package ru.bereshs.hhworksearch.service;

import com.github.scribejava.core.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.exception.HhworkSearchTokenException;
import ru.bereshs.hhworksearch.mapper.VacancyMapper;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhResumeDto;
import ru.bereshs.hhworksearch.producer.KafkaProducer;
import ru.bereshs.hhworksearch.service.impl.FilterEntityServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final VacancyEntityService vacancyEntityService;
    private final HhService service;
    private final KafkaProducer producer;
    private final ResumeEntityService resumeEntityService;
    private final MessageEntityService messageEntityService;
    private final EmployerEntityService employerEntityService;
    private final SkillEntityService skillsEntityService;
    private final VacancyFilterService vacancyFilterService;
    private final VacancyMapper mapper;
    private final FilterEntityServiceImpl filterEntityService;

    @Loggable
    public void dailyLightTaskRequest() throws InterruptedException, IOException, ExecutionException, HhWorkSearchException, HhworkSearchTokenException {

        List<VacancyEntity> vacancyList = getPageRecommendedVacancy(getKey());
        vacancyEntityService.saveAll(vacancyList);

        postNegotiationWithRelevantVacancies(vacancyList);
        updateResume();
    }

    @Loggable
    public void dailyFullRequest() throws InterruptedException, IOException, ExecutionException, HhWorkSearchException, HhworkSearchTokenException {
        List<VacancyEntity> vacancyList = getFullHhVacancy();
        postNegotiationWithRelevantVacancies(vacancyList);
        updateVacancyStatus();
        sendMessageDailyReport();
    }

    public String getKey() {
        return filterEntityService.getKey();

    }

    @Loggable
    public void dailyRecommendedRequest() throws InterruptedException, IOException, ExecutionException, HhWorkSearchException, HhworkSearchTokenException {
        List<VacancyEntity> vacancyList = service.getPageRecommendedVacancyForResume(resumeEntityService.getDefault())
                .getItems().stream().map(mapper::toVacancyEntity).toList();
        postNegotiationWithRelevantVacancies(vacancyList);
    }


    public void updateVacancyStatus() throws InterruptedException, IOException, ExecutionException, HhworkSearchTokenException {
        List<VacancyEntity> listFromNegotiations = service.getHhNegotiationsDtoList().getItems().stream().map(mapper::toVacancyEntity).toList();
        vacancyEntityService.updateVacancyStatusFromNegotiationsList(listFromNegotiations);
    }

    private void sendMessageDailyReport() throws HhWorkSearchException {
        String message = vacancyEntityService.getDaily();
        producer.produceDefault(message);
    }

    private void postNegotiationWithRelevantVacancies(List<VacancyEntity> vacancyList) throws InterruptedException, IOException, ExecutionException, HhWorkSearchException, HhworkSearchTokenException {
        if (vacancyList.isEmpty()) return;
        var filtered = getRelevantVacancies(vacancyList);
        postNegotiations(filtered);
        vacancyEntityService.changeAllStatus(filtered, VacancyStatus.REQUEST);
        vacancyEntityService.saveAll(filtered);
    }


    private void updateResume() throws IOException, ExecutionException, InterruptedException, HhworkSearchTokenException {
        ResumeEntity resume = resumeEntityService.getDefault();
        HhResumeDto resumeDto = service.updateResume(resume);
        resumeEntityService.setNextPublish(resume, resumeDto.getNextPublishAt());

    }

    private void postNegotiations(List<VacancyEntity> filtered) throws HhWorkSearchException, HhworkSearchTokenException {
        for (VacancyEntity element : filtered) {
            var vacancyEntity = vacancyEntityService.getById(element.getHhId());
            if (vacancyEntity.isPresent() && !vacancyEntity.get().isNotRequest()) {
                return;
            }

            List<SkillEntity> skills = skillsEntityService.extractVacancySkills(element);
            ResumeEntity resume = resumeEntityService.getRelevantResume(element);

            var body = messageEntityService.getNegotiationBody(skills, resume.getHhId(), element);
            Response response = service.postNegotiation(body);
            producer.produce(response, element.getHhId());


            //      negotiationsService.postNegotiationForVacancy(vacancy);
        }
    }

    @Loggable
    private List<VacancyEntity> getRelevantVacancies(List<VacancyEntity> list) throws InterruptedException, IOException, ExecutionException, HhworkSearchTokenException {
        List<VacancyEntity> filtered = list.stream().filter(vacancyFilterService::isSuitVacancy).toList();
        List<VacancyEntity> unique = vacancyEntityService.getUnique(filtered);
        List<VacancyEntity> full = service.getFullVacancyInformation(unique);
        return full.stream().filter(vacancyFilterService::isSuitVacancy).toList();

    }


    private List<VacancyEntity> getFullHhVacancy() throws InterruptedException {
        return getRecommendedVacancy();

    }


    @Loggable
    private List<VacancyEntity> getRecommendedVacancy() throws InterruptedException {
        try {
            var list = service.getRecommendedVacancy(getKey());
            var listEmployer = employerEntityService.extractEmployers(list);
            employerEntityService.saveAll(listEmployer);
            return mapper.toListVacancyEntity(list);
        } catch (IOException | ExecutionException | HhworkSearchTokenException e) {
            throw new RuntimeException(e);
        }
    }

    private List<VacancyEntity> getPageRecommendedVacancy(String key) throws InterruptedException {
        try {
            return mapper.toListVacancyEntity(service.getPageRecommendedVacancy(0, key).getItems());
        } catch (IOException | ExecutionException | HhworkSearchTokenException e) {
            throw new RuntimeException(e);
        }
    }

}
