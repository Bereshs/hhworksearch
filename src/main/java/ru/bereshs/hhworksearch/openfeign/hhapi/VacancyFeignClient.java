package ru.bereshs.hhworksearch.openfeign.hhapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.PathParams;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;

@FeignClient(name = "vacancyFeignClient", url = "${app.hh-api-uri}")
public interface VacancyFeignClient {

    @RequestMapping(value = "/vacancies/{id}", method = RequestMethod.GET)
    VacancyRs getVacancyById(@PathVariable("id") String id);

    @RequestMapping(value = "/resumes/{resume_id}/similar_vacancies")
    ListDto<VacancyRs> getSimilarVacancies(@PathVariable("resume_id") String resumeId);

    @RequestMapping(value = "/resumes/{resume_id}/similar_vacancies")
    ListDto<VacancyRs> getSimilarVacancies(@PathVariable("resume_id") String resumeId, @SpringQueryMap PathParams params);

    @RequestMapping(value = "/vacancies", method = RequestMethod.GET)
    ListDto<VacancyRs> getVacancies(@SpringQueryMap PathParams params);

}
