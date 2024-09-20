package ru.bereshs.hhworksearch.openfeign.hhapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ListDto<T>(
        Long found,
        Integer page,
        Integer pages,
        @JsonProperty("per_page")
        Integer perPage,
        List<T> items
) {
}
