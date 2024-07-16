package ru.bereshs.hhworksearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bereshs.hhworksearch.model.MessageEntity;

@Repository
public interface MessageEntityRepository extends JpaRepository<MessageEntity, Long> {
}
