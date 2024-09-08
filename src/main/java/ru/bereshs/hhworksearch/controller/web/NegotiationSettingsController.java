package ru.bereshs.hhworksearch.controller.web;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.SimpleDtoMapper;
import ru.bereshs.hhworksearch.model.MessageEntity;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;
import ru.bereshs.hhworksearch.service.impl.MessageEntityServiceImpl;
import ru.bereshs.hhworksearch.service.SkillEntityService;

import java.util.List;

@Controller
@AllArgsConstructor
public class NegotiationSettingsController {

    private final SkillEntityService skillsEntityService;
    private final MessageEntityServiceImpl messageEntityService;
    private final SimpleDtoMapper mapper;

    @GetMapping("/negotiationsettings")
    String getNegotiationSettingsPage(Model model, @RequestParam(value = "id", required = false) Long id) throws HhWorkSearchException {

        long messageId = 1L;
        if (id != null) messageId = id;

        MessageEntity message = messageEntityService.getById(messageId).orElseThrow(()->new HhWorkSearchException("Wrong parameters"));
        List<SkillEntity> skillsList = skillsEntityService.findAll();

        List<Long> list = messageEntityService.getAll().stream().map(MessageEntity::getId).sorted().toList();

        model.addAttribute("footer", new SimpleDto(message.getId(), null,
                message.getFooter() == null ? "Нет данных" : message.getFooter()));
        model.addAttribute("header", new SimpleDto(message.getId(), null,
                message.getHeader() == null ? "Нет данных" : message.getHeader()));
        model.addAttribute("skills", mapper.toSkillListSimpleDto(skillsList));
        model.addAttribute("negotiations", list);

        return "negotiationsettings";
    }
}
