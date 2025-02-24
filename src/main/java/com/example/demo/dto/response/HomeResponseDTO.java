package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeResponseDTO {
    private String leagueName;
    private String leagueLogo;
    private int season;
    private List<StandingResponseDTO> standingResponseDTOList;
}
