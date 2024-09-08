package ru.bereshs.hhworksearch.openfeign;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.bereshs.hhworksearch.config.CustomRequestInterceptor;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;

import java.net.URI;

@FeignClient(name="inner-client", url = "http://localhost:8080/api/client", configuration = CustomRequestInterceptor.class)
public interface InnerFeignClient {
    @GetMapping("/{id}")
    SimpleDto getDto(URI baseUrl);

}
