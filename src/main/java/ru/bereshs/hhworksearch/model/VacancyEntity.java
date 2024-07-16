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

public class VacancyEntity implements FilteredVacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String hhId;
    private String url;
    private String name;
    private String experience;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime published;
    private int responses;
    private String employerId;
    private String employerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VacancyStatus status;
    private LocalDateTime timeStamp;
    private LocalDateTime createdAt;
    private Integer salary;
    private String currency;

    public void setStatus(String status) {
        if (status.equals("response") || this.status.toString().length() < 3) {
            setStatus(VacancyStatus.REQUEST);
            return;
        }
        setStatus(VacancyStatus.valueOf(status.toUpperCase()));
    }

    public void setStatus(VacancyStatus status) {
        this.status = status;
    }

    public HhSalaryDto getSalary() {
        if (salary == null) salary = 0;
        return HhSalaryDto.builder()
                .currency(currency)
                .to(salary)
                .build();
    }



    @Override
    public List<String> getSkillStringList() {
        return null;
    }

    public void setDescription(String description) {
        if (description == null) {
            return;
        }
        String simpleString = description.toLowerCase().replaceAll("<[^>]*>", "").replaceAll("&quot;", "").replaceAll("&amp;", "").replaceAll("\\n", "");
        this.description = simpleString.length() > 255 ? simpleString.substring(0, 255) : simpleString;
    }

    public void setName(String name) {
        if (name == null) {
            return;
        }
        this.name = name.toLowerCase();
    }

    @Override
    public String toString() {
        return getHhId() + " " + getName() + " " + getEmployerId();
    }

    public HhSimpleListDto getEmployer() {
        return HhSimpleListDto.builder()
                .id(getEmployerId())
                .name(getEmployerName())
                .build();
    }

    public boolean isNotRequest() {
        return !getStatus().equals(VacancyStatus.request)
                && !getStatus().equals(VacancyStatus.REQUEST);
    }
}
