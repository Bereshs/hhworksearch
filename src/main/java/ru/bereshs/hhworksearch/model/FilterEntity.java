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
public class FilterEntity implements Comparable<FilterEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    private FilterScope scope;
    private String word;

    public String toString() {
        return "FilterEntity{scope=" + scope + ", word=" + word + "}";
    }

    @Override
    public int compareTo(FilterEntity filterEntity) {
        return word.compareTo(filterEntity.getWord());
    }
}
