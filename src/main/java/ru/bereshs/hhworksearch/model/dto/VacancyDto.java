package ru.bereshs.hhworksearch.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VacancyDto{
    private String name;
    private String employerId;
    private String employerName;
    private Long percent;
    private String createdAt;
    private String status;
    private String url;
    private String filterResult;
}
