package com.example.demo.domian;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class League {
    @Id
    private Long id;

    private String name;

    private String country;

    private String logo;

    private int season;

    @OneToMany(mappedBy = "league")
    private List<Standing> standings = new ArrayList<>();

    public void updateLeague(String name, String country, String logo, int season) {
        if(name != null) this.name = name;
        if(country != null) this.country = country;
        if(logo != null) this.logo = logo;
        if(season != 0) this.season = season;
    }

}
