package ru.bereshs.hhworksearch.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;
import ru.bereshs.hhworksearch.openfeign.InnerFeignClient;

import java.net.URI;

@Controller
@AllArgsConstructor
public class EditElementController {

    private final InnerFeignClient client;

    @GetMapping("/editelement")
    String editElementPage(@RequestParam(value = "name") String name, @RequestParam(value = "id") Long id, @RequestParam(value = "scope", required = false) String scope, Model model) {

        String path = "/api/client/" + name + "/";
        if (scope != null) {
            path += scope + "/";
        }

        path += id + "/";
        String ur = "http://192.168.1.14:8080" + path;

        URI url = URI.create(ur);
        SimpleDto simpleDto = client.getDto(url);


        model.addAttribute("dto", simpleDto);
        model.addAttribute("action", "/api/client/"+name+"/"+id+"/");
        model.addAttribute("target", name.equals("filter")?"/filtersettings":"/negotiationsettings");
        return "/editelement";
    }
}
