package ru.bereshs.hhworksearch.service;

import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.model.dto.ClientTokenDto;

import java.util.Map;

public interface AuthorizationClientService {

    ClientTokenDto getClientToken();

    ClientTokenDto getTokenFromCode(String code) throws HhWorkSearchException;
}
