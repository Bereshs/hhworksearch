package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.*;
import ru.bereshs.hhworksearch.model.SkillEntity;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SkillMapper {

    @Mapping(target = "id", ignore = true)
    void updateSkillEntity(@MappingTarget SkillEntity target, SkillEntity source);
}
