package ru.bereshs.hhworksearch.model;

import com.github.scribejava.core.model.OAuth2AccessToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.bereshs.hhworksearch.exception.HhWorkSearchException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "keys")
@Setter
@Getter
public class KeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime timeStamp;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private Long userId;


    public LocalDateTime getExpireIn() {
        if (expiresIn == null) {
            return LocalDateTime.now().minusDays(7);
        }
        return timeStamp.plusSeconds(expiresIn);
    }

    public void set(OAuth2AccessToken token) throws HhWorkSearchException {
        if (token == null ||
                token.getRefreshToken() == null ||
                token.getAccessToken() == null ||
                token.getAccessToken().equals("accessToken") ||
                token.getRefreshToken().equals("refreshToken") ||
                token.getExpiresIn() == null) {
            throw new HhWorkSearchException("Bad token credentials: accessToken: " + token.getAccessToken()
                    + ",refreshToken: " + token.getRefreshToken() + ", expiresIn: " + token.getExpiresIn());
        }

        setAccessToken(token.getAccessToken());
        setRefreshToken(token.getRefreshToken());
        setExpiresIn(Long.valueOf(token.getExpiresIn()));
    }


    public OAuth2AccessToken getOAuth2AccessToken() {
        return new OAuth2AccessToken(getAccessToken(),
                null,
                (int) getExpireIn().toEpochSecond(ZoneOffset.ofHours(3)),
                getRefreshToken(),
                null,
                null);
    }

}

