package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TeamInfoResponseDTO {

    private Long id;
    private String name;
    private String country;
    private String logo;
    private String city;
    private String venueName;
    private String venueImage;
}
