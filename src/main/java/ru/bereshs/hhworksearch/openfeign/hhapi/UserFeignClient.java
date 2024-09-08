package ru.bereshs.hhworksearch.openfeign.hhapi;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.UserDto;

import java.util.Optional;

@FeignClient(name = "userFeignClient", url = "${app.hh-api-uri}")
public interface UserFeignClient {

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    UserDto getMyPage();


}
