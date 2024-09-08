package ru.bereshs.hhworksearch.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bereshs.hhworksearch.repository.KeyEntityRepository;
import ru.bereshs.hhworksearch.model.KeyEntity;
import ru.bereshs.hhworksearch.service.KeyEntityService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KeyEntityServiceImpl implements KeyEntityService {
    private final KeyEntityRepository keysEntityRepository;


    public KeyEntity getByUserId(Long userId) {
        return keysEntityRepository.getByUserId(userId).orElse(new KeyEntity());
    }

    public boolean validateKey(KeyEntity key) {
        return !isExpired(key)
                && key.getAccessToken() != null
                && key.getRefreshToken() != null
                && !key.getAccessToken().equals("accessToken")
                && !key.getRefreshToken().equals("refreshToken");
    }

    public boolean isExpired(KeyEntity key) {
        return key != null &&
                key.getExpireIn() != null
                && !LocalDateTime.now().isBefore(key.getExpireIn());
    }

    public void save(KeyEntity key) {
        if (key == null || !validateKey(key)) {
            return;
        }
        key.setTimeStamp(LocalDateTime.now());
        keysEntityRepository.saveAndFlush(key);
    }

}
