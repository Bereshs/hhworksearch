package ru.bereshs.hhworksearch.openfeign.hhapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.EmployerDto;

@FeignClient(name = "employerFeignClient", url = "${app.hh-api-uri}")
public interface EmployerFeignClient {

    @RequestMapping(value = "/employers/{id}", method = RequestMethod.GET)
    EmployerDto getByHhId(String hhId);
}
