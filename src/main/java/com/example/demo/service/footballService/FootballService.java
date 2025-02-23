package com.example.demo.service.footballService;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface FootballService {

    void registerLeagueAndTeamStanding() throws JsonProcessingException;

    void updateTeamStatisticsAndStanding() throws JsonProcessingException;

    void updateTeamInitInfo() throws JsonProcessingException;
}
