package ru.bereshs.hhworksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bereshs.hhworksearch.model.ParameterEntity;
import ru.bereshs.hhworksearch.model.ParameterType;

import java.util.Optional;

@Repository
public interface ParameterEntityRepository extends JpaRepository<ParameterEntity, Long> {

    Optional<ParameterEntity> findByType(ParameterType type);
}
