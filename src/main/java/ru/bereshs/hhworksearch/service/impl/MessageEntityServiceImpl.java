package ru.bereshs.hhworksearch.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.mapper.MessageMapper;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.repository.MessageEntityRepository;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.service.MessageEntityService;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageEntityServiceImpl implements MessageEntityService {

    private final MessageEntityRepository repository;

    private final MessageMapper mapper;


    public HashMap<String, String> getNegotiationBody(List<SkillEntity> skills, String resumeId, VacancyEntity vacancy) throws HhWorkSearchException {
        String message = getNegotiationMessage(vacancy, skills);
        HashMap<String, String> body = new HashMap<>();
        body.put("message", message);
        body.put("resume_id", resumeId);
        body.put("vacancy_id", vacancy.getHhId());
        return body;
    }

    public void update(Long id, MessageEntity message) throws HhWorkSearchException {
        if (id == null || message == null) {
            throw new RuntimeException("Wrong parameters");
        }
        MessageEntity messageDb = getById(id).orElse(new MessageEntity());

        mapper.update(messageDb, message);

        save(messageDb);
    }

    public List<MessageEntity> getAll() {
        return repository.findAll();
    }

    public String getNegotiationMessage(VacancyEntity vacancy, List<SkillEntity> skills) throws HhWorkSearchException {
        return getById(1L).orElseThrow(() -> new HhWorkSearchException("Wrong id")).getMessage(skills, vacancy.getName());
    }

    public Optional<MessageEntity> getById(Long id) throws HhWorkSearchException {
        if (id == 0) {
            throw new HhWorkSearchException("Wrong parameter");
        }
        return repository.findById(id);
    }

    public void save(MessageEntity messageEntity) throws HhWorkSearchException {
        if (messageEntity == null) {
           return;
        }
        repository.save(messageEntity);
    }

    public void delete(MessageEntity message) throws HhWorkSearchException {
        if (message == null) {
            throw new HhWorkSearchException("Wrong parameter");
        }
        repository.delete(message);
    }
}
