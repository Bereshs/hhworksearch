package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.EmployerDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployerMapper {

    @Mapping(source = "id", target = "hhId")
    @Mapping(source = "alternateUrl", target = "url")
    @Mapping(source = "name", target = "name")
    EmployerEntity toEmployerEntity(EmployerDto dto);

    @Mapping(source = "id", target = "hhId")
    @Mapping(source = "alternateUrl", target = "url")
    @Mapping(source = "name", target = "name")
    List<EmployerEntity> toEmployerEntityList(List<EmployerDto> list);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    void updateEmployerEntity(@MappingTarget EmployerEntity target, EmployerEntity source);
}
