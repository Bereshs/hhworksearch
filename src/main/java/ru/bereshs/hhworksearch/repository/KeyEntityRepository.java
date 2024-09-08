package ru.bereshs.hhworksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bereshs.hhworksearch.model.KeyEntity;

import java.util.Optional;

public interface KeyEntityRepository extends JpaRepository<KeyEntity, Integer> {
    Optional<KeyEntity> getByUserId(Long userId);

}
