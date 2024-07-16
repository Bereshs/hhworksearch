package ru.bereshs.hhworksearch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bereshs.hhworksearch.hhapiclient.HhLocalDateTime;
import ru.bereshs.hhworksearch.hhapiclient.dto.HhResumeDto;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "resume")
@NoArgsConstructor
public class ResumeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String hhId;
    private String title;
    private String url;
    private boolean isDefault;
    private LocalDateTime timeStamp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String accessType;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime nextPublish;


}
