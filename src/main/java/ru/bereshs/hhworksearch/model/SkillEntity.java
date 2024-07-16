package ru.bereshs.hhworksearch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "skills")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Schema(description = "Хард скилы")
public class SkillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Schema(description = "Название хард скила")
    private String name;
    @Schema(description = "Описание хард скила (ставится в сопроводительное письмо)")
    private String description;

    public SkillEntity(String name) {
        this.name = name;
    }

    public String toString() {
        return name + "-" + description;
    }

}
