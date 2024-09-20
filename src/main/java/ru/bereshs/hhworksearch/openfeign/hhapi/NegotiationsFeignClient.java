package ru.bereshs.hhworksearch.openfeign.hhapi;


import feign.Param;
import feign.form.FormProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationMessageDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationRs;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@FeignClient(name = "negotiationsFeignClient", url = "${app.hh-api-uri}")
public interface NegotiationsFeignClient {

    @RequestMapping(value = "/negotiations", method = RequestMethod.GET)
    ListDto<NegotiationRs> getAllNegotiations();

    @RequestMapping(value = "/negotiations/{id}", method = RequestMethod.GET)
    NegotiationRs getNegotiationById(@PathVariable("id") String id);

    @RequestMapping(value = "/negotiations/{id}/messages", method = RequestMethod.POST)
    NegotiationRs postMessageToNegotiationById(@PathVariable("id") String id, @RequestBody String message);


    @RequestMapping(value = "/negotiations", method = RequestMethod.POST, consumes = APPLICATION_FORM_URLENCODED_VALUE)
    String postMessageToVacancy(Map<String, ?> map);


}
