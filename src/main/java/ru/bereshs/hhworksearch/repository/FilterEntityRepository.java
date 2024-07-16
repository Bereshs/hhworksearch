package ru.bereshs.hhworksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bereshs.hhworksearch.model.FilterEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilterEntityRepository extends JpaRepository<FilterEntity, Integer> {
    List<FilterEntity> findFilterEntityByScope(String scope);
    Optional<FilterEntity> findFilterEntityByScopeAndWord(String scope, String word);
}
