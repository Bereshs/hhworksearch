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

import java.util.HashMap;
import java.util.Map;


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
        //    log.info("refresh token={}", key.getRefreshToken());
        //if (keyEntityService.isExpired(key)) {
        //TokenRs tokenRs = oauthFeignClient.updateAccessAndRefreshTokens(new TokenRq(null, null, "refresh_token", key.getRefreshToken()));
        //   mapper.updateKeyEntity(key, tokenRs);
        //   keyEntityService.save(key);
        ClientTokenDto token = mapper.toClientTokenDto(key);
//        log.info("token info: access" + token.accessToken() + " refresh " + tokenRs.refreshToken());
        return token;
        //}
    }

    public ClientTokenDto getTokenFromCode(String code) throws HhWorkSearchException {
        TokenRs token = oauthFeignClient.updateAccessAndRefreshTokens(
                getBodyMapRequest(code));
        KeyEntity key = keyEntityService.getByUserId(1L);
        log.info("token {}",token);


        mapper.updateKeyEntity(key, token);

        log.info("key access {}, refresh {} expires {}", key.getAccessToken(), key.getRefreshToken(), key.getExpireIn());
        log.info("valid key {}", keyEntityService.validateKey(key));

        keyEntityService.save(key);
        return mapper.toClientTokenDto(key);
    }

    public Map<String, ?> getBodyMapRequest(String code) throws HhWorkSearchException {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", parameterEntityService.getByType(ParameterType.CLIENT_ID).getData());
        body.put("client_secret", parameterEntityService.getByType(ParameterType.CLIENT_SECRET).getData());
        body.put("grant_type", "authorization_code");
        body.put("code", code);
        return body;

    }


}
