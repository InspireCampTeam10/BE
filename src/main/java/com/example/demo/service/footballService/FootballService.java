package com.example.demo.service.footballService;

import com.example.demo.dto.request.LeagueInfoUpdateDTO;
import com.example.demo.dto.request.TeamInfoUpdateDTO;
import com.example.demo.dto.response.HomeResponseDTO;
import com.example.demo.dto.response.LeagueInfoResponseDTO;
import com.example.demo.dto.response.TeamInfoResponseDTO;
import com.example.demo.dto.response.TeamResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.servlet.http.HttpServletResponse;

public interface FootballService {

    void registerLeagueAndTeamStanding() throws JsonProcessingException;

    void updateTeamStatisticsAndStanding() throws JsonProcessingException;

    void updateTeamInitInfo();

    void updateTeamInfo(Long teamId, TeamInfoUpdateDTO teamInfoUpdateDTO);

    void updateLeagueInfo(LeagueInfoUpdateDTO leagueInfoUpdateDTO);

    HomeResponseDTO getHomeResponse();

    LeagueInfoResponseDTO getLeagueInfo();

    TeamInfoResponseDTO getTeamInfo(Long teamId);

    TeamResponseDTO getTeamStandingAndStatistics(Long teamId);

    HttpServletResponse getLeagueStandingCsv(HttpServletResponse response) throws Exception;
}
