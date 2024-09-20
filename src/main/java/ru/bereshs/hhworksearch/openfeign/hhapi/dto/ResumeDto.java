package ru.bereshs.hhworksearch.openfeign.hhapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResumeDto(
        String id,
        String title,
        @JsonProperty("alternate_url")
        String url,
        @JsonProperty("created_at")
        String createdAt,
        @JsonProperty("updated_at")
        String updatedAt,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("middle_name")
        String middleName,
        @JsonProperty("skill_set")
        List<String> skillSet,
        @JsonProperty("skills")
        String skills,
        boolean visible,
        @JsonProperty("next_publish_at")
        String nextPublish
) {
}
