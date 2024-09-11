package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.*;
import ru.bereshs.hhworksearch.model.FilterEntity;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface FilterMapper {

    @Mapping(target = "id", ignore = true)
    void updateFilter(@MappingTarget FilterEntity target, FilterEntity source);
}
