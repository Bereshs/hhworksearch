package ru.bereshs.hhworksearch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.SimpleDtoMapper;
import ru.bereshs.hhworksearch.model.MessageEntity;
import ru.bereshs.hhworksearch.model.ParameterEntity;
import ru.bereshs.hhworksearch.model.ParameterType;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;
import ru.bereshs.hhworksearch.service.ParameterEntityService;

@RestController
@RequiredArgsConstructor
public class RestParameterController {
    private final ParameterEntityService service;
    private final SimpleDtoMapper mapper;


    @GetMapping("/api/client/parameter/{type}/{id}/")
    SimpleDto getFooterHandler(@PathVariable("id") Long id, @PathVariable("type") ParameterType type) throws HhWorkSearchException {
        if (id == null || type == null) {
            throw new HhWorkSearchException("Wrong id");
        }

        if (id == 0) {
            return new SimpleDto(null, type.name(), "Parameter data");
        }

        ParameterEntity parameter = service.getByType(type);

        return mapper.toSimpleDto(parameter);
    }

    @PostMapping("/api/client/parameter/{id}/")
    ResponseEntity<SimpleDto> postParameterHandler(@PathVariable("id") Long id, SimpleDto dto) throws HhWorkSearchException {


        if (id == null || dto == null || dto.description() == null || dto.name() == null) {
            throw new HhWorkSearchException("Wrong parameters");
        }

        if (id == 0) {
            ParameterEntity parameter = new ParameterEntity();
            parameter.setType(ParameterType.valueOf(dto.name()));
            parameter.setData(dto.description());
            service.save(parameter);
        } else {
            ParameterEntity parameter = service.getByType(ParameterType.valueOf(dto.name()));
            parameter.setData(dto.description());
            service.update(parameter);
        }


        return ResponseEntity.ok(new SimpleDto(null, null, "ok"));

    }

}
