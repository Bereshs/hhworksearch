package ru.bereshs.hhworksearch.controller.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.dto.ClientTokenDto;
import ru.bereshs.hhworksearch.model.dto.ReportDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.ResumeFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.UserFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ResumeDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.UserDto;
import ru.bereshs.hhworksearch.service.*;
import ru.bereshs.hhworksearch.service.impl.KeyEntityServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {

    private final AuthorizationClientService authorizationClientService;
    private final ResumeFeignClient resumeFeignClient;
    private final UserFeignClient userFeignClient;

    private final KeyEntityServiceImpl keyEntityService;
    private final DateTimeFormatter formatter;
    private final DailyReportService reportService;
    private final VacancyEntityService vacancyEntityService;
    private final ParameterEntityService parameterService;


    @GetMapping("/")
    public String mainPage(Model model) throws HhWorkSearchException {
        KeyEntity key = keyEntityService.getByUserId(1L);

        if (parameterService.isUnCompleted()) {
            return "redirect:/parametersettings";
        }


        ParameterEntity parameter = parameterService.getByType(ParameterType.CLIENT_ID);



        if (keyEntityService.validateKey(key)) {
            UserDto userDto = userFeignClient.getMyPage();
            model.addAttribute("hhUserDto", userDto);
            return "redirect:/authorized";
        }


        String connectionString = "https://hh.ru/oauth/authorize?response_type=code&" + "client_id=" + parameter.getData();

        model.addAttribute("tokenLive", key.getExpireIn().format(formatter));
        model.addAttribute("connectionString", connectionString);
        return "index";
    }

    @GetMapping("/authorization")
    public String authorizationPage(@RequestParam(value = "code", required = false) String code, Model model) throws HhWorkSearchException {

        if (code == null) return "error";
        ClientTokenDto tokenDto = authorizationClientService.getTokenFromCode(code);
        createModel(model, tokenDto);
        return "authorized";
    }


    @RequestMapping("/authorized")
    public String authorizedPage(Model model) {
        ClientTokenDto tokenDto = authorizationClientService.getClientToken();
        createModel(model, tokenDto);

        return "authorized";
    }

    public void createModel(Model model, ClientTokenDto token) {
        ListDto<ResumeDto> myResumeList = resumeFeignClient.getResumeList();

        UserDto userDto = userFeignClient.getMyPage();

        List<VacancyEntity> daily = vacancyEntityService.getVacancyEntityByTimeStampAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
        ReportDto dailyReportDto = reportService.getReportDto(daily);
        List<VacancyEntity> full = vacancyEntityService.getAll();

        ReportDto fullReportDto = reportService.getReportDto(full);
        model.addAttribute("tokenLive", token.expiresIn().format(formatter));
        model.addAttribute("hhUserDto", userDto);
        model.addAttribute("resumeList", myResumeList.items());
        model.addAttribute("daily", dailyReportDto);
        model.addAttribute("full", fullReportDto);
    }


}
