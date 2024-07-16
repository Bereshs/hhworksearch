package ru.bereshs.hhworksearch.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bereshs.hhworksearch.model.SkillEntity;

import java.util.Optional;

@Repository
public interface SkillsEntityRepository extends JpaRepository<SkillEntity, Long> {
    Optional<SkillEntity> getSkillsEntityByName(String name);

}
