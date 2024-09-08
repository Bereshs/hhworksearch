package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.model.dto.SimpleDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SimpleDtoMapper {


    SimpleDto toSimpleDto(SkillEntity skillEntity);

    List<SimpleDto> toSkillListSimpleDto(List<SkillEntity> skillEntityList);

    List<SimpleDto> fromListParameter(List<ParameterEntity> list);

    @Mapping(source = "scope", target = "description", ignore = true)
    @Mapping(source = "word", target = "name")
    SimpleDto toSimpleDto(FilterEntity filterEntity);

    @Mapping(source = "name", target = "name")
    SimpleDto toSimpleDto(EmployerEntity employerEntity);

    @Mapping(source = "type", target = "name")
    @Mapping(source = "data", target = "description")
    SimpleDto toSimpleDto(ParameterEntity entity);

    SkillEntity toSkillEntity(SimpleDto dto);


    @Mapping(source = "description", target = "scope")
    @Mapping(source = "name", target = "word")
    FilterEntity toFilterEntity(SimpleDto dto);
}
