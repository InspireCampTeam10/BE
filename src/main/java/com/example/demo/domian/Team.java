package com.example.demo.domian;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    private Long id;

    private String name;
    private String country;
    private String logo;
    private String city;
    private String venueName;
    private String venueImage;

    @OneToMany(mappedBy = "team")
    private List<TeamStatistics> teamStatistics = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Standing> standings = new ArrayList<>();

    public void updateTeam(String name, String country, String logo, String city, String venueName, String venueImage) {
        if(name != null) this.name = name;
        if(country != null) this.country = country;
        if(logo != null) this.logo = logo;
        if(city != null) this.city = city;
        if(venueName != null) this.venueName = venueName;
        if(venueImage != null) this.venueImage = venueImage;
    }

    public void updateTeamInit(String country, String city, String venueName, String venueImage) {
        this.country = country;
        this.city = city;
        this.venueName = venueName;
        this.venueImage = venueImage;
    }

}
