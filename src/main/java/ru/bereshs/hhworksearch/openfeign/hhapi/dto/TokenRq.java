package ru.bereshs.hhworksearch.openfeign.hhapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenRq(
        @JsonProperty("client_id")
        String clientId,
        @JsonProperty("client_secret")
        String clientSecret,
        @JsonProperty("grant_type")
        String grantType,

        @JsonProperty("refresh_token")
        String refreshToken
) {
}
