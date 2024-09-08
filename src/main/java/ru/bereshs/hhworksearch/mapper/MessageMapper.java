package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.*;
import ru.bereshs.hhworksearch.model.MessageEntity;

import java.util.Map;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface MessageMapper {

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget MessageEntity target, MessageEntity source);

}
