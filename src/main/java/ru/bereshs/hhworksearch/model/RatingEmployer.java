package ru.bereshs.hhworksearch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "rating_employer")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class RatingEmployer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String employerId;
    private Double rating;

    public RatingEmployer(String employerId, Double rating) {
        this.employerId = employerId;
        this.rating = rating;
    }
    public String toString() {
        return "employerId: "+employerId+", rating: "+rating;
    }
}
