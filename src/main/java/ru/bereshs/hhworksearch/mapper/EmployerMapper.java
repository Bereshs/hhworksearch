package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;
import ru.bereshs.hhworksearch.model.EmployerEntity;

@Mapper(componentModel = "spring")
public interface EmployerMapper {

    @Mapping(source = "id", target = "hhId")
    EmployerEntity toEmployerEntity(HhSimpleListDto listDto);


}
