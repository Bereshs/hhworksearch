package ru.bereshs.hhworksearch.openfeign.hhapi.dto;


public record NegotiationMessageDto(
        String message,
        String resume_id,
        String vacancy_id
) {
}
