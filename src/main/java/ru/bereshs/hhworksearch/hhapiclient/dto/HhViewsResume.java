package ru.bereshs.hhworksearch.hhapiclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class HhViewsResume implements HasEmployer{
    @JsonProperty("created_at")
    private String createdAt;
    private String viewed;
    @JsonProperty("employer")
    private HhSimpleListDto employer;
}
