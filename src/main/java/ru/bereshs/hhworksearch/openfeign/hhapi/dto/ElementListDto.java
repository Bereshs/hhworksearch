package ru.bereshs.hhworksearch.openfeign.hhapi.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElementListDto(
        String id,
        String name,
        @JsonProperty("alternate_url")
        String alternateUrl,
        String url,
        Boolean active,
        @JsonProperty("total_responses")
        Integer totalResponses,
        Integer from,
        Integer to,
        String currency,
        Boolean gross

) {
}
