package com.example.demo.controller;

import com.example.demo.dto.response.HomeResponseDTO;
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

    @PostMapping("/init")
    public ApiResponse<?> registerLeagueAndTeamInit() throws JsonProcessingException {
        footballService.registerLeagueAndTeamStanding();
        return ApiResponse.onSuccess("초기 리그 순위 및 팀 통계 정보 등록 성공");
    }

    @PatchMapping("/update")
    public ApiResponse<?> updateStanding() throws JsonProcessingException {
        footballService.updateTeamStatisticsAndStanding();
        return ApiResponse.onSuccess("순위 및 통계 정보 최신화 성공");
    }

    @PatchMapping("/team/init")
    public ApiResponse<?> updateTeamInitInfo() throws JsonProcessingException {
        footballService.updateTeamInitInfo();
        return ApiResponse.onSuccess("팀 상세 정보 등록 성공");
    }
}