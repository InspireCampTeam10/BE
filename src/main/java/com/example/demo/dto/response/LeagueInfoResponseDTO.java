package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LeagueInfoResponseDTO {
    private String name;
    private String country;
    private String logo;
    private int season;
}
