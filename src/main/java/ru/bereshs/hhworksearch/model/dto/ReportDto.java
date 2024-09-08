package ru.bereshs.hhworksearch.model.dto;

import java.util.List;

public record ReportDto(
        Long requested,
        Long invited,
        Long discarded,
        Long founded,
        Long total,
        String salary
) {
}
