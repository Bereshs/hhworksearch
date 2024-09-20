package ru.bereshs.hhworksearch.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bereshs.hhworksearch.model.KeyEntity;
import ru.bereshs.hhworksearch.model.dto.ClientTokenDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.TokenRs;

@Mapper(componentModel = "spring")
public interface TokenMapper {


    @Mapping(target = "expiresIn", expression = "java(key.getExpireIn())")
    ClientTokenDto toClientTokenDto(KeyEntity key);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refreshToken", expression = "java(rs.refreshToken().length()>10?rs.refreshToken():null)")
    @Mapping(target = "accessToken", expression = "java(rs.accessToken().length()>10?rs.accessToken():null)")
    void updateKeyEntity(@MappingTarget KeyEntity key, TokenRs rs);
}
