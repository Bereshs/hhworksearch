package ru.bereshs.hhworksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bereshs.hhworksearch.model.RatingEmployer;

import java.util.Optional;

@Repository
public interface RatingEmployerRepository extends JpaRepository<RatingEmployer, Integer> {
    Optional<RatingEmployer> findByEmployerId(String employerId);

}
