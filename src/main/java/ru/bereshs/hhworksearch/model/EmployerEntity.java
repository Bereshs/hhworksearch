package ru.bereshs.hhworksearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhSimpleListDto;

@Entity
@Setter
@Getter
@Table(name = "employer")
@NoArgsConstructor
public class EmployerEntity implements Comparable<EmployerEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hhId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("alternate_url")
    private String alternateUrl;

    @Override
    public int compareTo(EmployerEntity employerEntity) {
        return getHhId().compareTo(employerEntity.getHhId());
    }
}
