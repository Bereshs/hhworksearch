package ru.bereshs.hhworksearch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.SimpleDtoMapper;
import ru.bereshs.hhworksearch.model.MessageEntity;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;
import ru.bereshs.hhworksearch.service.MessageEntityService;

@RestController
@RequiredArgsConstructor
public class RestMessageController {
    private final MessageEntityService service;
    private final SimpleDtoMapper mapper;

    @GetMapping("/api/client/footer/{id}/")
    SimpleDto getFooterHandler(@PathVariable("id") Long id) throws HhWorkSearchException {
        if (id == null) {
            throw new HhWorkSearchException("Wrong id");
        }
        if (id == 0) {
            return new SimpleDto(null, null, "Ваш текст");
        }

        MessageEntity message = service.getById(id);
        if(message.getFooter()==null) {
            message.setFooter("Нет данных");
        }

        return new SimpleDto(message.getId(), null, message.getFooter());
    }

    @DeleteMapping("/api/client/footer/{id}/")
    ResponseEntity<SimpleDto> deleteFooterHandler(@PathVariable("id") Long id) throws HhWorkSearchException {

        deleteMessage(id);
        return ResponseEntity.ok(new SimpleDto(null, null, "ok"));
    }

    @DeleteMapping("/api/client/header/{id}/")
    ResponseEntity<SimpleDto> deleteHeaderHandler(@PathVariable("id") Long id) throws HhWorkSearchException {

        deleteMessage(id);
        return ResponseEntity.ok(new SimpleDto(null, null, "ok"));
    }

    @PostMapping("/api/client/footer/{id}/")
    ResponseEntity<SimpleDto> postFooterHandler(@PathVariable("id") Long id, SimpleDto dto) throws HhWorkSearchException {

        if (id == null || dto == null) {
            throw new HhWorkSearchException("Wrong parameters");
        }

        if (id == 0 && dto.description() != null) {
            MessageEntity message = new MessageEntity();
            message.setFooter(dto.description());
            service.save(message);
        } else {
            MessageEntity message = service.getById(id);
            message.setFooter(dto.description());
            service.update(id, message);
        }


        return ResponseEntity.ok(new SimpleDto(null, null, "ok"));
    }

    @GetMapping("/api/client/header/{id}/")
    SimpleDto getHeaderHandler(@PathVariable("id") Long id) throws HhWorkSearchException {
        if (id == null) {
            throw new HhWorkSearchException("Wrong id");
        }
        if (id == 0) {
            return new SimpleDto(null, null, "Ваш текст");
        }

        MessageEntity message = service.getById(id);

        if(message.getHeader()==null) {
            message.setHeader("Нет данных");
        }
        return new SimpleDto(message.getId(), null, message.getHeader());
    }

    @PostMapping("/api/client/header/{id}/")
    ResponseEntity<SimpleDto> postHeaderHandler(@PathVariable("id") Long id, SimpleDto dto) throws HhWorkSearchException {

        if (id == null || dto == null) {
            throw new HhWorkSearchException("Wrong parameters");
        }

        if (id == 0 && dto.description() != null) {
            MessageEntity message = new MessageEntity();
            message.setHeader(dto.description());
            service.save(message);
        } else {
            MessageEntity message = service.getById(id);
            message.setHeader(dto.description());
            service.update(id, message);
        }
        return ResponseEntity.ok(new SimpleDto(null, null, "ok"));
    }

    void deleteMessage(Long id) throws HhWorkSearchException {
        if (id == null || id == 0) {
            throw new HhWorkSearchException("Wrong parameters");
        }

        MessageEntity message = service.getById(id);

        service.delete(message);


    }
}
