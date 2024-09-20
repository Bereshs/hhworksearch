package ru.bereshs.hhworksearch.openfeign.hhapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bereshs.hhworksearch.config.CustomRequestInterceptor;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.TokenRq;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.TokenRs;

@FeignClient(value = "oauthFeignClient", url = "${app.hh-api-uri}",  configuration = CustomRequestInterceptor.class)
public interface OauthFeignClient {

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    TokenRs updateAccessAndRefreshTokens(@RequestBody TokenRq request);


}
