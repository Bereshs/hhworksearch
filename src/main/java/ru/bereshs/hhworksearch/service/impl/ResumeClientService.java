package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.aop.Loggable;
import ru.bereshs.hhworksearch.mapper.ResumeMapper;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.ResumeFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ResumeDto;
import ru.bereshs.hhworksearch.service.ResumeEntityService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeClientService {
    private final ResumeEntityService service;
    private final ResumeFeignClient client;
    private final ResumeMapper mapper;

    @Loggable
    public void updateResume() {
        ResumeEntity resume = service.getDefault();

        if (resume.getNextPublish().isBefore(LocalDateTime.now())) {
            client.updateResumeById(resume.getHhId());
        }

        ResumeDto resumeDto = getResumeFromClient(resume.getHhId());
        mapper.updateResumeEntity(resume, resumeDto);
        service.save(resume);
    }

    public ResumeEntity getDefaultResume() {
        return service.getDefault();
    }

    public ResumeDto getResumeFromClient(String hhId) {
        return client.getResumeById(hhId);
    }

    public ResumeEntity toResumeEntity(ResumeDto resumeDto) {
        return mapper.toResumeEntity(resumeDto);
    }
}
