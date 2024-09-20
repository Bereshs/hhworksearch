package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bereshs.hhworksearch.model.VacancyEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ElementListDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.NegotiationRs;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.VacancyRs;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VacancyRsMapper {


    @Mapping(target = "hhId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(rs.name().toLowerCase())")
    @Mapping(target = "experience", source = "experience.id")
    @Mapping(source = "counters.totalResponses", target = "responses")
    @Mapping(target = "timeStamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "employerId", source = "employer.id")
    @Mapping(target = "status", expression = "java(ru.bereshs.hhworksearch.model.VacancyStatus.FOUND)")
    @Mapping(target = "currency", source = "salary.currency")
    @Mapping(target = "published", source = "publishedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Mapping(target = "salary", expression = "java(salaryCalculator(rs.salary()))")
    @Mapping(target = "description", expression = "java(descriptionConverter(rs.description()))")
    @Mapping(target = "skillStringList", expression = "java(toList(rs.skills()))")
    @Mapping(target = "url", source = "alternateUrl")
    VacancyEntity toVacancyEntity(VacancyRs rs);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(ru.bereshs.hhworksearch.model.VacancyStatus.valueOf(dto.state().id().toUpperCase()))")
    @Mapping(source = "vacancy.id", target = "hhId")
    @Mapping(target = "name", ignore = true)
    @Mapping(source = "vacancy.employer.id", target = "employerId")
    VacancyEntity toVacancyEntity(NegotiationRs dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "status", expression = "java(ru.bereshs.hhworksearch.model.VacancyStatus.valueOf(rs.state().id().toUpperCase()))")
    void updateVacancyEntity(@MappingTarget VacancyEntity vacancy, NegotiationRs rs);

    List<VacancyEntity> toListVacancyEntity(List<VacancyRs> vacancyList);


    @Mapping(target = "experience", ignore = true)
    @Mapping(target = "salary", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "skillStringList", expression = "java(toList(rs.skills()))")
    void updateVacancyEntity(@MappingTarget VacancyEntity vacancy, VacancyRs rs);


    List<VacancyEntity> toVacancyEnityList(List<VacancyRs> rs);

    default String toList(List<ElementListDto> list) {
        if (list == null) {
            return "";
        }
        return list.stream().map(e -> e.name().toLowerCase()).collect(Collectors.joining(","));
    }

    default String descriptionConverter(String description) {
        if (description == null) {
            return "";
        }
        return description.replaceAll("<[^>]*>", "").replaceAll("\\s{2,}", " ").toLowerCase();
    }

    default Integer salaryCalculator(ElementListDto element) {
        if (element == null) {
            return 0;
        }
        if (element.from() != null && element.to() != null) {
            return ((element.from() + element.to()) / 2);
        }
        if (element.from() != null) {
            return element.from();
        }
        if (element.to() != null) {
            return element.to();
        }
        return 0;
    }
}
