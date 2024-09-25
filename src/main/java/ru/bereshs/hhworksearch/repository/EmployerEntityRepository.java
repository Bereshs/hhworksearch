package ru.bereshs.hhworksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bereshs.hhworksearch.model.EmployerEntity;

import java.util.Optional;

public interface EmployerEntityRepository extends JpaRepository<EmployerEntity, Integer> {
    Optional<EmployerEntity> getByHhId(String hhId);

    boolean existsByHhId(String hhId);

}
