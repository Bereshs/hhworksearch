package ru.bereshs.hhworksearch.hhapiclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HhNegotiationsDto {
    @JsonProperty("created_at")
    private String createdAt;
    private String id;
    @JsonProperty("state")
    private HhSimpleListDto state;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("vacancy")
    private HhVacancyDto vacancy;
    @JsonProperty("has_updates")
    private boolean hasUpdates;
}
