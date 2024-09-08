package ru.bereshs.hhworksearch.model.dto;

import java.time.LocalDateTime;

public record ClientTokenDto (
        String accessToken,
        String refreshToken,
        LocalDateTime expiresIn
)
{
}
