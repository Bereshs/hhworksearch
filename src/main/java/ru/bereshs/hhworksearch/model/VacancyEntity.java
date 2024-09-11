package ru.bereshs.hhworksearch.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSalaryDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "vacancy")
@NoArgsConstructor
@Slf4j
@Schema(description = "Вакансия")

public class VacancyEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hhId;
    private String url;
    private String name;
    private String experience;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime published;
    private int responses;
    private String employerId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VacancyStatus status;
    private LocalDateTime timeStamp;
    private Integer salary;
    private String currency;
    private String skillStringList;

    @Transient
    private Long percent;
    @Transient
    private String filterResult;
    @Override
    public String toString() {
        return getHhId() + " " + getName() + " " + getEmployerId();
    }


    public boolean isNotRequest() {
        return !getStatus().equals(VacancyStatus.request)
                && !getStatus().equals(VacancyStatus.REQUEST);
    }
}
