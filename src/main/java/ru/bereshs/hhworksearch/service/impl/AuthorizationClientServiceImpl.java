package ru.bereshs.hhworksearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.TokenMapper;
import ru.bereshs.hhworksearch.model.KeyEntity;
import ru.bereshs.hhworksearch.model.ParameterType;
import ru.bereshs.hhworksearch.model.dto.ClientTokenDto;
import ru.bereshs.hhworksearch.openfeign.hhapi.OauthFeignClient;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.TokenRq;
import ru.bereshs.hhworksearch.openfeign.hhapi.dto.TokenRs;
import ru.bereshs.hhworksearch.service.AuthorizationClientService;
import ru.bereshs.hhworksearch.service.ParameterEntityService;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationClientServiceImpl implements AuthorizationClientService {
    private final TokenMapper mapper;
    private final OauthFeignClient oauthFeignClient;
    private final KeyEntityServiceImpl keyEntityService;
    private final ParameterEntityService parameterEntityService;

    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.application.version}")
    private String appVersion;

    @Override
    public ClientTokenDto getClientToken() {
        KeyEntity key = keyEntityService.getByUserId(1L);
        if (keyEntityService.validateKey(key)) {
            return mapper.toClientTokenDto(key);
        }
        //if (keyEntityService.isExpired(key)) {
        TokenRs tokenRs = oauthFeignClient.updateAccessAndRefreshTokens(new TokenRq(null, null, "refresh_token", key.getRefreshToken()));
        mapper.updateKeyEntity(key, tokenRs);
        keyEntityService.save(key);
        ClientTokenDto token = mapper.toClientTokenDto(key);
        log.info("token info: access" + token.accessToken() + " refresh " + tokenRs.refreshToken());
        return token;
        //}
    }

    public ClientTokenDto getTokenFromCode(String code) throws HhWorkSearchException {
        TokenRs token = oauthFeignClient.updateAccessAndRefreshTokens(
                new TokenRq(parameterEntityService.getByType(ParameterType.CLIENT_ID).getData(),
                        parameterEntityService.getByType(ParameterType.CLIENT_SECRET).getData(),
                        "authorization_code", code));


        KeyEntity key = keyEntityService.getByUserId(1L);
        mapper.updateKeyEntity(key, token);
        keyEntityService.save(key);
        return mapper.toClientTokenDto(key);
    }


}