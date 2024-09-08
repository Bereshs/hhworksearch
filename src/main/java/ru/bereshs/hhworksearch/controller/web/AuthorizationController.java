package ru.bereshs.hhworksearch.controller.web;

import com.github.scribejava.core.model.OAuth2AccessToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bereshs.hhworksearch.config.AppConfig;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhResumeDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhUserDto;
import ru.bereshs.hhworksearch.model.dto.ReportDto;
import ru.bereshs.hhworksearch.service.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Controller
@AllArgsConstructor
@Slf4j
public class AuthorizationController {

    private final HhService service;
    private final KeyEntityService keyEntityService;
    private final DateTimeFormatter formatter;
    private final DailyReportService reportService;
    private final VacancyEntityService vacancyEntityService;
    private final ParameterEntityService parameterService;
    private final AppConfig config;

    @GetMapping("/")
    public String mainPage(Model model) throws HhWorkSearchException {
        KeyEntity key = keyEntityService.getByUserId(1L);

        if (parameterService.isUnCompleted()) {
            return "redirect:/parametersettings";
        }


        ParameterEntity parameter = parameterService.getByType(ParameterType.CLIENT_ID);


        if (keyEntityService.validateKey(key)) {
            HhUserDto hhUserDto = new HhUserDto();
            model.addAttribute("hhUserDto", hhUserDto);
            return "redirect:/authorized";
        }


        String connectionString = config.getHhApiAuthorization() + "?response_type=code&" + "client_id=" + parameter.getData();

        model.addAttribute("tokenLive", key.getExpireTime().format(formatter));
        model.addAttribute("connectionString", connectionString);
        return "/index";
    }

    @GetMapping("/authorization")
    public String authorizationPage(@RequestParam(value = "code", required = false) String code, Model model) throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        if (code == null) return "error";
        KeyEntity key = keyEntityService.getByUserId(1L);
        OAuth2AccessToken token = service.getAccessToken(code);
        keyEntityService.saveToken(key, token);
        createModel(model, token);
        return "authorized";
    }


    @RequestMapping("/authorized")
    public String authorizedPage(Model model) throws IOException, ExecutionException, InterruptedException {
        OAuth2AccessToken token = service.getToken();
        try {
            createModel(model, token);
        } catch (HhWorkSearchException e) {
            keyEntityService.invalidToken(1L);
            return "redirect:/";
        }
        return "authorized";
    }

    public void createModel(Model model, OAuth2AccessToken token) throws IOException, ExecutionException, InterruptedException, HhWorkSearchException {
        HhListDto<HhResumeDto> myResumeList = service.getActiveResumes();

        HhUserDto hhUserDto = new HhUserDto();
        var mePageBody = service.getMePageBody();

        if (mePageBody.get("description") != null && mePageBody.get("description").equals("Forbidden")) {
            throw new HhWorkSearchException("HhService authorization error");
        }
        hhUserDto.set(mePageBody);
        List<VacancyEntity> daily = vacancyEntityService.getVacancyEntityByTimeStampAfter(LocalDateTime.now().minusDays(1));
        ReportDto dailyReportDto = reportService.getReportDto(daily);
        List<VacancyEntity> full = vacancyEntityService.getAll();

        ReportDto fullReportDto = reportService.getReportDto(full);
        model.addAttribute("tokenLive", formatter.format(Instant.ofEpochSecond(token.getExpiresIn())));
        model.addAttribute("hhUserDto", hhUserDto);
        model.addAttribute("resumeList", myResumeList.getItems());
        model.addAttribute("daily", dailyReportDto);
        model.addAttribute("full", fullReportDto);
    }


}
