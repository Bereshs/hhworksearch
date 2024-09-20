package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.model.ResumeEntity;

public interface ResumeEntityService {
    ResumeEntity getDefault();
    ResumeEntity getByHhId(String hhId);
    ResumeEntity getById(Long id);
    void save(ResumeEntity resume) ;

}
