package ru.bereshs.hhworksearch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.EnumUtils;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.SimpleDtoMapper;
import ru.bereshs.hhworksearch.model.FilterEntity;
import ru.bereshs.hhworksearch.model.FilterScope;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;
import ru.bereshs.hhworksearch.service.FilterEntityService;

@RestController
@RequiredArgsConstructor
public class RestFilterController {


    private final FilterEntityService service;
    private final SimpleDtoMapper mapper;

    @DeleteMapping("/api/client/filter/{id}/")
    ResponseEntity<SimpleDto> deleteFilterHandler(@PathVariable("id") Long id) throws HhWorkSearchException {
        if (id == null || id == 0) {
            throw new HhWorkSearchException("Wrong parameters");
        }
        FilterEntity filterEntity = service.getById(id);

        service.delete(filterEntity);

        return ResponseEntity.ok(new SimpleDto(null, null, "ok"));

    }

    @GetMapping("/api/client/filter/{scope}/{id}/")
    SimpleDto getNewFilterHandler(@PathVariable("id") Long id, @PathVariable("scope") String scope) throws HhWorkSearchException {

        if (id == null || id > 0 || scope == null || !isValidScope(scope)) {
            throw new HhWorkSearchException("Wrong parameters");
        }

        return new SimpleDto(null, "Enter Name", scope);
    }

    @GetMapping("/api/client/filter/{id}/")
    SimpleDto getFilterHandler(@PathVariable("id") Long id) throws HhWorkSearchException {
        if (id == null) {
            throw new HhWorkSearchException("Wrong id");
        }
        FilterEntity filterEntity;
        if (id.equals(0L)) {
            filterEntity = new FilterEntity();
        } else {
            filterEntity = service.getById(id);
        }

        return mapper.toSimpleDto(filterEntity);
    }


    @PostMapping("/api/client/filter/{id}/")
    ResponseEntity<SimpleDto> postFilterHandler(@PathVariable("id") Long id, SimpleDto dto) throws HhWorkSearchException {
        if (id == null || dto == null) {
            throw new HhWorkSearchException("Wrong parameters");
        }

        if (id == 0) {
            FilterEntity filterEntity = mapper.toFilterEntity(dto);
            service.save(filterEntity);
        } else {
            service.update(id, mapper.toFilterEntity(dto));
        }
        return ResponseEntity.ok(
                new SimpleDto(null, null, "ok")
        );
    }


    boolean isValidScope(String scope) {
        try {
            FilterScope fs = FilterScope.valueOf(scope);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

}
