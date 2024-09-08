package ru.bereshs.hhworksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bereshs.hhworksearch.model.FilterEntity;
import ru.bereshs.hhworksearch.model.FilterScope;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilterEntityRepository extends JpaRepository<FilterEntity, Long> {
    List<FilterEntity> findFilterEntityByScope(FilterScope scope);
    Optional<FilterEntity> findFilterEntityByScopeAndWord(FilterScope scope, String word);
}
