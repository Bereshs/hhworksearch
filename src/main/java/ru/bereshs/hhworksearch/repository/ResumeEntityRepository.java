package ru.bereshs.hhworksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bereshs.hhworksearch.model.ResumeEntity;

import java.util.Optional;

@Repository
public interface ResumeEntityRepository extends JpaRepository<ResumeEntity, Integer> {
    Optional<ResumeEntity> getResumeEntityByHhId(String hhId);
    Optional<ResumeEntity> getById(long id);

}
