package ru.bereshs.hhworksearch.service;


import com.github.scribejava.core.model.OAuth2AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.mapper.AppMapper;
import ru.bereshs.hhworksearch.repository.KeyEntityRepository;
import ru.bereshs.hhworksearch.model.KeyEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class KeyEntityService {
    private final KeyEntityRepository keysEntityRepository;


    public KeyEntity getByUserId(Long userId) {
        return keysEntityRepository.getByUserId(userId).orElse(new KeyEntity());
    }

    public OAuth2AccessToken getToken() throws HhWorkSearchException {
        KeyEntity key = keysEntityRepository.getByUserId(1L).orElseThrow(() -> new HhWorkSearchException("Token not found"));
        if (!key.isValid()) throw new HhWorkSearchException("Token is invalid");
        long expirationTime = key.getTime().plusSeconds(key.getExpiresIn()).toEpochSecond(ZoneOffset.ofHours(3));
        return new OAuth2AccessToken(key.getAccessToken(),
                key.getTokenType(),
                (int) expirationTime,
                key.getRefreshToken(),
                key.getScope(),
                key.getRowResponse());
    }

    public void saveToken(KeyEntity key, OAuth2AccessToken token) {
        key.set(token);
        key.setTime(LocalDateTime.now());
        save(key);
    }


    public boolean validateKey(KeyEntity key) {
        return key.isValid();
    }

    public void invalidToken(long id) {
        KeyEntity key = getByUserId(id);
        key.setExpiresIn(null);
        save(key);
    }

    public void save(KeyEntity key) {
        keysEntityRepository.saveAndFlush(key);
    }

}
