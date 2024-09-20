package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ElementListDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployerMapper {

    @Mapping(source = "id", target = "hhId")
    EmployerEntity toEmployerEntity(ElementListDto listDto);
    List<EmployerEntity> toEmployerEntityList(List<ElementListDto> list);


}
