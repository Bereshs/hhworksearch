package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhNegotiationsDto;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhVacancyDto;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.dto.VacancyDto;
import ru.bereshs.hhworksearch.service.EmployerEntityService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VacancyMapper {


    @Mapping(source = "published", target = "createdAt")
    VacancyDto toVacancyDto(VacancyEntity vacancy);

    @Mapping(source = "name", target = "employerName")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "url", ignore = true)
    void updateVacancyDto(@MappingTarget VacancyDto target, EmployerEntity employer);


    List<VacancyDto> toVacancyDtoList(List<VacancyEntity> list);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(vacancyDto.getName().toLowerCase())")
    @Mapping(source = "id", target = "hhId")
    @Mapping(source = "alternateUrl", target = "url")
    @Mapping(source = "counters.totalResponses", target = "responses")
    @Mapping(target = "timeStamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(source = "employer.id", target = "employerId")
    @Mapping(expression = "java(ru.bereshs.hhworksearch.model.VacancyStatus.FOUND)", target = "status")
    @Mapping(source = "salary.currency", target = "currency")
    @Mapping(expression = "java(java.time.LocalDateTime.parse(vacancyDto.getPublishedAt().substring(0, vacancyDto.getPublishedAt().length() - 5)))", target = "published")
    @Mapping(target = "salary", expression = "java(vacancyDto.getSalary()==null?0:vacancyDto.getSalary().getFrom()>0 && vacancyDto.getSalary().getTo()>0?(vacancyDto.getSalary().getTo()+vacancyDto.getSalary().getFrom())/2 : " +
            "vacancyDto.getSalary().getFrom()>0?vacancyDto.getSalary().getFrom():vacancyDto.getSalary().getTo())")
    @Mapping(target = "description", expression = "java(vacancyDto.getDescription()!=null?vacancyDto.getDescription().replaceAll(\"[^A-Za-zА-Яа-я0-9\\s]\",\"\").replaceAll(\"  \",\"\"):null)")
    VacancyEntity toVacancyEntity(HhVacancyDto vacancyDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(ru.bereshs.hhworksearch.model.VacancyStatus.valueOf(dto.getState().getId().toUpperCase()))")
    @Mapping(source = "vacancy.id", target = "hhId")
    @Mapping(target = "name", ignore = true)
    @Mapping(source = "vacancy.employer.id", target = "employerId")
    VacancyEntity toVacancyEntity(HhNegotiationsDto dto);

    List<VacancyEntity> toListVacancyEntity(List<HhVacancyDto> vacancyList);

    void updateVacancyEntity(@MappingTarget VacancyEntity target, VacancyEntity source);

    default String toString(List<String> str) {
        return String.join(",", str).toLowerCase();
    }

    default String toString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault());
        return dateTime.format(formatter);
    }


}
