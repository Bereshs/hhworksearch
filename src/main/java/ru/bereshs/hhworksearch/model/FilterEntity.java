package ru.bereshs.hhworksearch.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "filter")
@Entity
@Getter
@Setter
@Schema(description = "Фильтр")
public class FilterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String scope;
    private String word;

    public String toString() {
        return "FilterEntity{scope=" + scope + ", word=" + word + "}";
    }
}
