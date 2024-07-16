package ru.bereshs.hhworksearch.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TelegramMessageDto {
    private String token;
    private String chatId;
    private String message;
    private LocalDateTime time;
}
