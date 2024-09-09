package ru.bereshs.hhworksearch.mapper;

import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.hhapiclient.HhLocalDateTime;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhResumeDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSalaryDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.model.*;
import ru.bereshs.hhworksearch.model.dto.SettingsDto;

import java.time.LocalDateTime;

@Service
public class AppMapperImpl implements AppMapper {

    @Override
    public EmployerEntity toEmployerEntity(HhSimpleListDto employerDto) {
        EmployerEntity entity = new EmployerEntity();
        entity.setHhId(employerDto.getId());
        entity.setName(employerDto.getName());
        entity.setUrl(employerDto.getUrl());
        entity.setAlternateUrl(employerDto.getAlternateUrl());
        return entity;
    }

    @Override
    public ResumeEntity toResumeEntity(HhResumeDto resumeDto) {
        ResumeEntity resume = new ResumeEntity();
        resume.setHhId(resumeDto.getId());
        resume.setTitle(resumeDto.getTitle());
        resume.setUrl(resumeDto.getUrl());
        resume.setCreatedAt(HhLocalDateTime.decodeLocalData(resumeDto.getCreatedAt()));
        resume.setUpdatedAt(HhLocalDateTime.decodeLocalData(resumeDto.getUpdatedAt()));
        resume.setTimeStamp(LocalDateTime.now());
        resume.setNextPublish(HhLocalDateTime.decodeLocalData(resumeDto.getNextPublishAt()));

        return resume;
    }

    @Override
    public VacancyEntity toVacancyEntity(HhVacancyDto vacancyDto) {
        VacancyEntity vacancy=new VacancyEntity();
        vacancy.setHhId(vacancyDto.getId());
        vacancy.setUrl(vacancyDto.getAlternateUrl());
        vacancy.setName(vacancyDto.getName());
        vacancy.setDescription(vacancyDto.getDescription());
        vacancy.setPublished(LocalDateTime.parse(toDateWithoutTimezone(vacancyDto.getPublishedAt())));
        vacancy.setResponses(vacancyDto.getCounters() == null ? 0 : vacancyDto.getCounters().getTotalResponses());
        vacancy.setEmployerId(vacancyDto.getEmployer().getId());
        vacancy.setEmployerName(vacancyDto.getEmployer().getName());
        vacancy.setStatus(VacancyStatus.FOUND);
        vacancy.setExperience(vacancyDto.getExperience());
        vacancy.setTimeStamp(LocalDateTime.now());
        vacancy.setCreatedAt(LocalDateTime.now());
        vacancy.setSalary(toInt(vacancyDto.getSalary()));
        vacancy.setCurrency(vacancyDto.getSalary() == null ? "none" : vacancyDto.getSalary().getCurrency());

        return vacancy;
    }

    @Override
    public String toDateWithoutTimezone(String date) {
        return date.substring(0, date.length() - 5);
    }

    @Override
    public Integer toInt(HhSalaryDto salaryDto) {
        if (salaryDto == null) {
            return 0;
        }
        if (salaryDto.getFrom() == 0 && salaryDto.getTo() == 0) return 0;
        if (salaryDto.getFrom() > 0 && salaryDto.getTo() > 0) return (salaryDto.getFrom() + salaryDto.getTo()) / 2;
        if (salaryDto.getFrom() == 0 && salaryDto.getTo() > 0) return salaryDto.getTo();
        if (salaryDto.getFrom() > 0 && salaryDto.getTo() == 0) return salaryDto.getFrom();
        return 0;
    }
}
