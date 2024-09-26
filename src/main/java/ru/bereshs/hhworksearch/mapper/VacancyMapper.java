package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.*;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import ru.bereshs.hhworksearch.model.EmployerEntity;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.model.dto.VacancyDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationRs;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VacancyMapper {


    @Mapping(source = "published", target = "createdAt")
    @Mapping(target = "percent", source = "percent")
    VacancyDto toVacancyDto(VacancyEntity vacancy);

    @Mapping(source = "name", target = "employerName")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "url", ignore = true)
    void updateVacancyDto(@MappingTarget VacancyDto target, EmployerEntity employer);



    List<VacancyDto> toVacancyDtoList(List<VacancyEntity> list);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(vacancyDto.name().toLowerCase())")
    @Mapping(source = "id", target = "hhId")
    @Mapping(source = "alternateUrl", target = "url")
    @Mapping(source = "counters.totalResponses", target = "responses")
    @Mapping(target = "timeStamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(source = "employer.id", target = "employerId")
    @Mapping(expression = "java(ru.bereshs.hhworksearch.model.VacancyStatus.FOUND)", target = "status")
    @Mapping(source = "salary.currency", target = "currency")
    @Mapping(expression = "java(java.time.LocalDateTime.parse(vacancyDto.publishedAt().substring(0, vacancyDto.publishedAt().length() - 5)))", target = "published")
    @Mapping(target = "salary", expression = "java(vacancyDto.salary()==null?0:vacancyDto.salary().from()>0 && vacancyDto.salary().to()>0?(vacancyDto.salary().to()+vacancyDto.salary().from())/2 : " +
            "vacancyDto.salary().from()>0?vacancyDto.salary().from():vacancyDto.salary().to())")
    @Mapping(target = "description", expression = "java(vacancyDto.description()!=null?vacancyDto.description().replaceAll(\"[^A-Za-zА-Яа-я0-9\\s]\",\"\").replaceAll(\"  \",\"\"):null)")
    @Mapping(target = "experience", source = "experience.id")
    VacancyEntity toVacancyEntity(VacancyRs vacancyDto);




    @Mapping(target = "name", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status")
    void updateVacancyEntity(@MappingTarget VacancyEntity target, VacancyEntity source);

    default String toString(List<String> str) {
        return String.join(",", str).toLowerCase();
    }

    default String toString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault());
        return dateTime.format(formatter);
    }


}
