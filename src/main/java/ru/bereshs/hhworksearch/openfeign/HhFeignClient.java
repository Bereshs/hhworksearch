package ru.bereshs.hhworksearch.openfeign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hh-client", url = "https://api.hh.ru/")
public interface HhFeignClient {
}
