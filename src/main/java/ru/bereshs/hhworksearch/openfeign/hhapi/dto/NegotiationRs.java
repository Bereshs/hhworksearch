package ru.bereshs.hhworksearch.openfeign.hhapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NegotiationRs(
        @JsonProperty("created_at")
        String createdAt,
        String id,
        @JsonProperty("state")
        ElementListDto state,
        @JsonProperty("updated_at")
        String updatedAt,
        @JsonProperty("vacancy")
        VacancyRs vacancy,
        @JsonProperty("has_updates")
        boolean hasUpdates
) {
}
