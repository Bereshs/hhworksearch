package ru.bereshs.hhworksearch.repository;

import io.swagger.v3.oas.models.info.License;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.VacancyStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VacancyEntityRepository extends JpaRepository<VacancyEntity, Long> {
    VacancyEntity getByHhId(String hhid);

    VacancyEntity findFirstBy(Sort sort);

    List<VacancyEntity> getVacancyEntitiesByTimeStampAfter(LocalDateTime date);

    List<VacancyEntity> findVacancyEntitiesByStatus(VacancyStatus status);
}
