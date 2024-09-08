package ru.bereshs.hhworksearch.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimpleDto(
        Long id,
        String name,
        String description

) {
}
