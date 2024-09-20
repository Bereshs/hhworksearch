package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bereshs.hhworksearch.model.ResumeEntity;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.ResumeDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hhId", source = "id")
    @Mapping(target = "default", ignore = true)
    @Mapping(target = "timeStamp", ignore = true)
    @Mapping(target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Mapping(target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Mapping(target = "nextPublish", dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Mapping(target = "description", source = "skills")
    @Mapping(target = "skills", source = "skillSet")
    ResumeEntity toResumeEntity(ResumeDto dto);



    void updateResumeEntity(@MappingTarget ResumeEntity target, ResumeDto source);
    default String toString(List<String> list) {
        if (list == null) {
            return "";
        }
        return String.join(",", list);
    }

    List<ResumeEntity> toResumeList(List<ResumeDto> list);
}
