package ru.bereshs.hhworksearch.openfeign.hhapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmployerDto(
        @JsonProperty("alternate_url")
        String alternateUrl,
        String description,
        String id,
        String name
) {
}
