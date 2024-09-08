package ru.bereshs.hhworksearch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.SimpleDtoMapper;
import ru.bereshs.hhworksearch.model.SkillEntity;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;
import ru.bereshs.hhworksearch.service.impl.SkillEntityServiceImpl;

@RestController
@RequiredArgsConstructor
public class RestSkillController {

    private final SimpleDtoMapper mapper;
    private final SkillEntityServiceImpl service;

    @DeleteMapping("/api/client/skill/{id}/")
    ResponseEntity<SimpleDto> deleteSkillHandler(@PathVariable("id") Long id) throws HhWorkSearchException {
        if (id == null || id == 0) {
            throw new HhWorkSearchException("Wrong id");
        }

        SkillEntity skill = service.getById(id).orElseThrow(() -> new HhWorkSearchException("Wrong parameters"));
        service.delete(skill);

        return ResponseEntity.ok(new SimpleDto(null, null, "ok"));
    }

    @GetMapping("/api/client/skill/{id}/")
    SimpleDto getSkillHandler(@PathVariable("id") Long id) throws HhWorkSearchException {
        if (id == null) {
            throw new HhWorkSearchException("Wrong id");
        }

        SkillEntity skillEntity = service.getById(id).orElse(new SkillEntity("Ключевое слово", "Описание"));
        return mapper.toSimpleDto(skillEntity);
    }


    @PostMapping("/api/client/skill/{id}/")
    ResponseEntity<SimpleDto> postSkillHandler(@PathVariable("id") Long id, SimpleDto dto) throws HhWorkSearchException {
        if (id == null || dto == null) {
            throw new HhWorkSearchException("Wrong parameters");
        }

        service.update(id, mapper.toSkillEntity(dto));

        return ResponseEntity.ok(
                new SimpleDto(null, null, "Ok")
        );
    }


}
