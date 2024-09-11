package ru.bereshs.hhworksearch.hhapiclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

@Slf4j
public class HhVacancyDto implements HasEmployer {
    String id;
    String name;
    HhSimpleListDto area;
    HhCountersDto counters;
    HhSimpleListDto employer;
    HhSalaryDto salary;
    HhSimpleListDto experience;
    @JsonProperty("published_at")
    String publishedAt;
    @JsonProperty("apply_alternate_url")
    String url;
    LocalDateTime createdAt;
    String description;
    @JsonProperty("alternate_url")
    String alternateUrl;
    String urlRequest;
    @JsonProperty("key_skills")
    List<HhSimpleListDto> skills;

    public String getExperience() {
        if (experience == null) {
            experience = new HhSimpleListDto();
        }
        return experience.getId();
    }



    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }

        this.description = description.toLowerCase().replaceAll("<[^>]*>", "").replaceAll("&quot;", "").replaceAll("&amp;", "").replaceAll("\\n", "");

    }

    public HhCountersDto getCounters() {
        if (counters == null) {
            return new HhCountersDto();
        }
        return counters;
    }


    public List<String> getSkillStringList() {
        if (skills == null) {
            return null;
        }
        return skills.stream().map(HhSimpleListDto::getName).toList();
    }


    @Override
    public String toString() {
        return "name:" + name
                + " description:" + description
                + " counters " + counters
                + " skills " + skills;
    }

}
