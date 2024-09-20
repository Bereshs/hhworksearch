package ru.bereshs.hhworksearch.openfeign.hhapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ResumeDto;

import java.util.Optional;


@FeignClient(value = "resumeFeignClient", url = "${app.hh-api-uri}")
public interface ResumeFeignClient {

    @RequestMapping(value = "/resumes/mine", method = RequestMethod.GET)
    ListDto<ResumeDto> getResumeList();

    @RequestMapping(value = "/resumes/{id}", method = RequestMethod.GET)
    ResumeDto getResumeById(@PathVariable String id);

    @RequestMapping(value = "/resumes/{id}/publish", method = RequestMethod.POST)
    ResumeDto updateResumeById(@PathVariable String id);

}
