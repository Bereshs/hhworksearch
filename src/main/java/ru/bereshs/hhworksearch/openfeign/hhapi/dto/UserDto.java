package ru.bereshs.hhworksearch.openfeign.hhapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDto(
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("middle_name")
        String middleName,
        String phone,
        String email,
        String id
) {
}
