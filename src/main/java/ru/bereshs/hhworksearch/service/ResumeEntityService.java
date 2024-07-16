package ru.bereshs.hhworksearch.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.hhapiclient.HhLocalDateTime;
import ru.bereshs.hhworksearch.repository.ResumeEntityRepository;
import ru.bereshs.hhworksearch.model.FilteredVacancy;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhResumeDto;

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

    public ResumeEntity getByHhid(String hhId) {
        return resumeEntityRepository.getResumeEntityByHhId(hhId).orElse(new ResumeEntity());
    }

    public ResumeEntity getById(long id) {
        return resumeEntityRepository.getById(id).orElse(new ResumeEntity());
    }

    public ResumeEntity getRelevantResume(FilteredVacancy vacancy) {
        return getDefault();
    }

    public void save(ResumeEntity resume) {
        resume.setTimeStamp(LocalDateTime.now());
        resumeEntityRepository.save(resume);
    }

    public void saveAll(List<ResumeEntity> list) {
        list.forEach(this::save);
    }


    public void setDefault(ResumeEntity resume) {
        List<ResumeEntity> list = resumeEntityRepository.findAll();
        list.forEach(element -> {
            element.setDefault(element.getHhId().equals(resume.getHhId()));
        });
        saveAll(list);
    }
}
