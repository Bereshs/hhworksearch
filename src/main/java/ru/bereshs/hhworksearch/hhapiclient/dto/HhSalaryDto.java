package ru.bereshs.hhworksearch.hhapiclient.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HhSalaryDto {
    int from;
    int to;
    String currency;
    boolean gross;
}
