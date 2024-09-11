package ru.bereshs.hhworksearch.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.hhapiclient.HhLocalDateTime;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.repository.ResumeEntityRepository;
import ru.bereshs.hhworksearch.model.ResumeEntity;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ResumeEntityService {
    private final ResumeEntityRepository resumeEntityRepository;

    public void setNextPublish(ResumeEntity resume, String hhData) {
        resume.setNextPublish(HhLocalDateTime.decodeLocalData(hhData));
        save(resume);
    }

    public ResumeEntity getDefault() {
        return resumeEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "isDefault")).get(0);
    }

    public ResumeEntity getByHhId(String hhId) {
        return resumeEntityRepository.getResumeEntityByHhId(hhId).orElse(new ResumeEntity());
    }

    public ResumeEntity getById(Long id) {
        if(id==null) {
            return new ResumeEntity();
        }
        return resumeEntityRepository.getById(id).orElse(new ResumeEntity());
    }

    public ResumeEntity getRelevantResume(VacancyEntity vacancy) {
        return getDefault();
    }

    public void save(ResumeEntity resume) {
        if(resume==null) {
            return;
        }
        resume.setTimeStamp(LocalDateTime.now());
        resumeEntityRepository.save(resume);
    }

    public void saveAll(List<ResumeEntity> list) {
        if(list==null || list.isEmpty()) {
            return;
        }

        list.forEach(this::save);
    }


    public void setDefault(ResumeEntity resume) {
        if(resume==null) {
            return;
        }
        List<ResumeEntity> list = resumeEntityRepository.findAll();
        list.forEach(element -> {
            element.setDefault(element.getHhId().equals(resume.getHhId()));
        });
        saveAll(list);
    }
}
