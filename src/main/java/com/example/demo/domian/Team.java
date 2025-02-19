package com.example.demo.domian;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String logo;
    private String city;
    private String venueName;
    private String venueImage;

    @OneToMany(mappedBy = "team")
    private List<TeamStatistics> teamStatistics;
}
