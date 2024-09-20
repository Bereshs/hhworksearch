package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.NegotiationsFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationMessageDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationRs;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.PathParams;
import ru.bereshs.hhworksearch.service.MessageEntityService;
import ru.bereshs.hhworksearch.service.SkillEntityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NegotiationsClientService {

    private final NegotiationsFeignClient negotiationsFeignClient;
    private final MessageEntityService messageEntityService;
    private final SkillEntityService skillsEntityService;
    private final ResumeClientService resumeClientService;


    @Loggable
    public void postNegotiations(List<VacancyEntity> filteredNotRequested) throws HhWorkSearchException {
        if (filteredNotRequested == null || filteredNotRequested.isEmpty()) {
            return;
        }
        ResumeEntity defaultResume = resumeClientService.getDefaultResume();
        for (VacancyEntity e : filteredNotRequested) {
            List<String> skills = skillsEntityService.foundAllSkills(e);

            if (skills == null || skills.isEmpty()) {
                return;
            }
            List<SkillEntity> skillEntities = skillsEntityService.getSkillEntityList(skills);
            String txt = messageEntityService.getNegotiationMessage(e, skillEntities);
            NegotiationMessageDto messageDto = new NegotiationMessageDto(txt, defaultResume.getHhId(), e.getHhId());
            negotiationsFeignClient.postMessageToVacancy(getMapBody(messageDto));
        }

    }

    public Map<String, ?> getMapBody(NegotiationMessageDto messageDto) {
        Map<String, String> result = new HashMap<>();
        result.put("message", messageDto.message());
        result.put("resume_id", messageDto.resume_id());
        result.put("vacancy_id", messageDto.vacancy_id());
        return result;
    }

    @Loggable
    public ListDto<NegotiationRs> getAllNegotiations() {
        return negotiationsFeignClient.getAllNegotiations(PathParams.builder()
                .per_page(100)
                .period(1)
                .page(0)
                .build());
    }
}
