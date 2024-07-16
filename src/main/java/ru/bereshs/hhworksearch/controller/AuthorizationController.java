package ru.bereshs.hhworksearch.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bereshs.hhworksearch.config.AppConfig;
import ru.bereshs.hhworksearch.model.KeyEntity;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhResumeDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhUserDto;
import ru.bereshs.hhworksearch.service.*;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

@Controller
@AllArgsConstructor
@Slf4j
public class AuthorizationController {

    private final HhService service;
    private final AppConfig config;
    private final KeyEntityService keyEntityService;
    private final ResumeEntityService resumeEntityService;


    @GetMapping("/")
    public String mainPage(Model model) {
        KeyEntity key = keyEntityService.getByUserId(1L);

        if (keyEntityService.validateKey(key)) {
            HhUserDto hhUserDto = new HhUserDto();
            model.addAttribute("hhUserDto", hhUserDto);
            return "redirect:/authorized";
        }

        model.addAttribute("connectionString", config.getAuthorizationConnectionString());
        return "/index";
    }

    @GetMapping("/authorization")
    public String authorizationPage(String code, Model model) throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        if (code == null) return "error";
        KeyEntity key = keyEntityService.getByUserId(1L);
        OAuth2AccessToken token = service.getAccessToken(code);
        keyEntityService.saveToken(key, token);
        createModel(model, token);
        return "authorized";
    }


    @RequestMapping("/authorized")
    public String authorizedPage(Model model, @RequestParam(required = false) Integer page) throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        OAuth2AccessToken token = service.getToken();
        createModel(model, token);
        return "authorized";
    }

    public void createModel(Model model, OAuth2AccessToken token) throws IOException, ExecutionException, InterruptedException {
        ResumeEntity defaultResume = resumeEntityService.getDefault();
        HhListDto<HhResumeDto> myResumeList = service.getActiveResumes();

        HhUserDto hhUserDto = new HhUserDto();
        hhUserDto.set(service.getMePageBody());

        HhListDto<HhVacancyDto> vacancyList = service.getPageRecommendedVacancyForResume(defaultResume);
        vacancyList.setItems(vacancyList.getItems());

        model.addAttribute("tokenLive", Instant.ofEpochSecond(token.getExpiresIn()));
        model.addAttribute("hhUserDto", hhUserDto);
        model.addAttribute("resumeList", myResumeList.getItems());
        model.addAttribute("vacancyList", vacancyList);

    }


}
