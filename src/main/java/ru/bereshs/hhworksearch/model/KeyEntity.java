package ru.bereshs.hhworksearch.model;

import com.github.scribejava.core.model.OAuth2AccessToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "keys")
@Setter
@Getter
public class KeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private LocalDateTime time;
    private String authorizationCode;
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private String tokenType;
    private String scope;
    private String clientId;
    private String rowResponse;
    private Long userId;
    public boolean isValid() {
        if (expiresIn == null || authorizationCode == null) {
            return false;
        }
        LocalDateTime expireTime = getExpireTime();
        return LocalDateTime.now()
                .isBefore(expireTime);
    }

    public LocalDateTime getExpireTime() {
        if(expiresIn==null) {
            return LocalDateTime.now().minusDays(7);
        }
        return time.plusSeconds(expiresIn);//-3*60*60*24
    }
    public void set(OAuth2AccessToken token) {
        setAccessToken(token.getAccessToken());
        setRefreshToken(token.getRefreshToken());
        setExpiresIn(token.getExpiresIn());
        setTokenType(token.getTokenType());
        setScope(token.getScope());
        setRowResponse(token.getRawResponse());
    }
}
