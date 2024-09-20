package ru.bereshs.hhworksearch.openfeign.hhapi.dto;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VacancyRs(
        String id,
        String name,
        ElementListDto area,
        ElementListDto counters,
        ElementListDto employer,
        ElementListDto salary,
        ElementListDto experience,
        @JsonProperty("published_at")
        String publishedAt,
        @JsonProperty("alternate_url")
        String alternateUrl,
        String description,
        String urlRequest,
        @JsonProperty("key_skills")
        List<ElementListDto> skills

) {
}
