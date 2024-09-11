package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.MessageEntity;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface MessageEntityService {
    HashMap<String, String> getNegotiationBody(List<SkillEntity> skills, String resumeId, VacancyEntity vacancy) throws HhWorkSearchException;

    void update(Long id, MessageEntity message) throws HhWorkSearchException;

    List<MessageEntity> getAll();

    String getNegotiationMessage(VacancyEntity vacancy, List<SkillEntity> skills) throws HhWorkSearchException;

    Optional<MessageEntity> getById(Long id) throws HhWorkSearchException;

    void save(MessageEntity messageEntity) throws HhWorkSearchException;

    void delete(MessageEntity message) throws HhWorkSearchException;
}
