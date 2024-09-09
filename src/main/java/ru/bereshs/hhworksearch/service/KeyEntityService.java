package ru.bereshs.hhworksearch.service;


import com.github.scribejava.core.model.OAuth2AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;
import ru.bereshs.hhworksearch.repository.KeyEntityRepository;
import ru.bereshs.hhworksearch.model.KeyEntity;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KeyEntityService {
    private final KeyEntityRepository keysEntityRepository;


    public KeyEntity getByUserId(Long userId) {
        return keysEntityRepository.getByUserId(userId).orElse(new KeyEntity());
    }

    public OAuth2AccessToken getToken() throws HhWorkSearchException {
        KeyEntity key = keysEntityRepository.getByUserId(1L).orElseThrow(
                () -> new HhWorkSearchException("Token not found"));
        if (!validateKey(key)) {
            throw new HhWorkSearchException("Token is invalid");
        }

        return key.getOAuth2AccessToken();
    }

    public void saveToken(KeyEntity key, OAuth2AccessToken token) throws HhWorkSearchException {
        key.set(token);
        if (validateKey(key)) {
            save(key);
        }
    }


    public boolean validateKey(KeyEntity key) {
        return key.getExpireIn() != null
                && LocalDateTime.now().isBefore(key.getExpireIn())
                && key.getAccessToken() != null
                && key.getRefreshToken() != null
                && !key.getAccessToken().equals("accessToken")
                && !key.getRefreshToken().equals("refreshToken");
    }

    public void save(KeyEntity key) {
        key.setTimeStamp(LocalDateTime.now());
        keysEntityRepository.saveAndFlush(key);
    }

}
