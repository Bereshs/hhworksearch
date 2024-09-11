package ru.bereshs.hhworksearch.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bereshs.hhworksearch.model.VacancyEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VacancyEntityRepository extends JpaRepository<VacancyEntity, Long> {
    Optional<VacancyEntity> getByHhId(String hhid);

    VacancyEntity findFirstBy(Sort sort);

    List<VacancyEntity> getVacancyEntitiesByTimeStampAfter(LocalDateTime date);
}
