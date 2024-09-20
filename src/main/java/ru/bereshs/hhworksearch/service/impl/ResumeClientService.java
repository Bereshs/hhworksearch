package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.mapper.ResumeMapper;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.ResumeFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ResumeDto;
import ru.bereshs.hhworksearch.service.ResumeEntityService;

@Service
@RequiredArgsConstructor
public class ResumeClientService {
    private final ResumeEntityService service;
    private final ResumeFeignClient client;
    private final ResumeMapper mapper;

    @Loggable
    public void updateResume() {
        ResumeEntity resume = service.getDefault();
        ResumeDto resumeDto = client.updateResumeById(resume.getHhId());
        mapper.updateResumeEntity(resume, resumeDto);
        service.save(resume);
    }

    public ResumeEntity getDefaultResume() {
        return service.getDefault();
    }
}
