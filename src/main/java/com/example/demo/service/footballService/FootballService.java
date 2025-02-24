package com.example.demo.service.footballService;

import com.example.demo.dto.response.HomeResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface FootballService {

    void registerLeagueAndTeamStanding() throws JsonProcessingException;

    void updateTeamStatisticsAndStanding() throws JsonProcessingException;

    void updateTeamInitInfo();

    HomeResponseDTO getHomeResponse();
}
