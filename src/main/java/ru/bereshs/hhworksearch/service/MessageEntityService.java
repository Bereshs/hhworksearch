package ru.bereshs.hhworksearch.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.repository.MessageEntityRepository;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;


import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageEntityService {

    private final MessageEntityRepository messageEntityRepository;


    public HashMap<String, String> getNegotiationBody(List<SkillEntity> skills, String resumeId, HhVacancyDto vacancy) {
        String message = getNegotiationMessage(vacancy, skills);
        HashMap<String, String> body = new HashMap<>();
        body.put("message", message);
        body.put("resume_id", resumeId);
        body.put("vacancy_id", vacancy.getId());
        return body;
    }

    public void update(Long id, MessageEntity message) throws HhWorkSearchException {
        if (id == null || message == null) {
            throw new RuntimeException("Wrong parameters");
        }
        MessageEntity messageDb = getById(id);

        if (message.getFooter() != null && !message.getFooter().equals(messageDb.getFooter())) {
            messageDb.setFooter(message.getFooter());
        }

        if (message.getHeader() != null && !message.getHeader().equals(messageDb.getHeader())) {
            messageDb.setHeader(message.getHeader());
        }

        save(messageDb);
    }

    public List<MessageEntity> getAll() {
        return messageEntityRepository.findAll();
    }

    public String getNegotiationMessage(FilteredVacancy vacancy, List<SkillEntity> skills) {
        MessageEntity message = getMessage(1);
        if (skills.isEmpty()) {
            message = getMessage(2);
        }
        return message.getMessage(skills, vacancy.getName());
    }

    private MessageEntity getMessage(long id) {
        try {
            return getById(id);
        } catch (HhWorkSearchException e) {
            throw new RuntimeException(e);
        }
    }

    public MessageEntity getById(long id) throws HhWorkSearchException {
        return messageEntityRepository.findById(id).orElseThrow(() -> new HhWorkSearchException("Wrong message id"));
    }

    public void save(MessageEntity messageEntity) {
        messageEntityRepository.save(messageEntity);
    }

    public void patchMessageById(long id, MessageEntity messageDto) throws HhWorkSearchException {
        MessageEntity message = getById(id);
        message.setHeader(messageDto.getHeader());
        message.setFooter(messageDto.getFooter());
        save(message);
    }

    public void delete(MessageEntity message) {
        messageEntityRepository.delete(message);
    }
}
