package ru.bereshs.hhworksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bereshs.hhworksearch.model.EmployerEntity;

public interface EmployerEntityRepository extends JpaRepository<EmployerEntity, Integer> {
    EmployerEntity getByHhId(String hhId);

    boolean existsByHhId(String hhId);

}
