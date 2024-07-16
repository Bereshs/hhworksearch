package ru.bereshs.hhworksearch.hhapiclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HhCountersDto {
    @JsonProperty("total_responses")
    int totalResponses;

    public String toString() {
        return String.valueOf(totalResponses);
    }

    public Integer value() {
        return totalResponses;
    }
 }
