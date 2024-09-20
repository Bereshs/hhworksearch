package ru.bereshs.hhworksearch.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.repository.ResumeEntityRepository;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.service.ResumeEntityService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ResumeEntityServiceImpl implements ResumeEntityService {
    private final ResumeEntityRepository resumeEntityRepository;


    public ResumeEntity getDefault() {
        return resumeEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "isDefault")).get(0);
    }

    public ResumeEntity getByHhId(String hhId) {
        return resumeEntityRepository.getResumeEntityByHhId(hhId).orElse(new ResumeEntity());
    }

    public ResumeEntity getById(Long id) {
        if (id == null) {
            return new ResumeEntity();
        }
        return resumeEntityRepository.getById(id).orElse(new ResumeEntity());
    }

    public void save(ResumeEntity resume) {
        if (resume == null) {
            return;
        }
        resume.setTimeStamp(LocalDateTime.now());
        resumeEntityRepository.save(resume);
    }


}
