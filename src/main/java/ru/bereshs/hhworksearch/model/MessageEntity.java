package ru.bereshs.hhworksearch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "message")
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Сопроводительное письмо")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(length = 2048)
    @Schema(description = "Начальная часть письма")
    private String header;
    @Column(length = 2048)
    @Schema(description = "Заключительная часть письма")
    private String footer;

    public String getMessage(List<SkillEntity> skills, String vacancyName) {
        StringBuilder competitions = new StringBuilder();
        for (SkillEntity element : skills) {
            if (element.getDescription() != null) {
                competitions.append(element.getDescription()).append(" ");
            }
        }
        return header.replace("<VacancyName>", vacancyName) + competitions + footer;
    }
}
