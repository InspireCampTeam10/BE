package com.example.demo.controller;

import com.example.demo.dto.response.HomeResponseDTO;
import com.example.demo.dto.response.TeamResponseDTO;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.service.footballService.FootballService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/football")
public class FootballController {

    private final FootballService footballService;

    @GetMapping("/home")
    public ApiResponse<HomeResponseDTO> getTeam(){
        HomeResponseDTO homeResponse = footballService.getHomeResponse();
        return ApiResponse.onSuccess(homeResponse);
    }

    @GetMapping("/team/{teamId}")
    public ApiResponse<TeamResponseDTO> getTeam(@PathVariable Long teamId){
        return ApiResponse.onSuccess(footballService.getTeamStandingAndStatistics(teamId));
    }


}