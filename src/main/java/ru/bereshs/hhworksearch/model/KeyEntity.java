package ru.bereshs.hhworksearch.model;
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




}

