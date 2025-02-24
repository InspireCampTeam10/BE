package com.example.demo.controller;

import com.example.demo.dto.request.LeagueInfoUpdateDTO;
import com.example.demo.dto.request.TeamInfoUpdateDTO;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.service.footballService.FootballService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final FootballService footballService;

    @GetMapping("")
    public String adminP(){
        return "Admin Controller";
    }


    @PostMapping("/football/init")
    public ApiResponse<?> registerLeagueAndTeamInit() throws JsonProcessingException {
        footballService.registerLeagueAndTeamStanding();
        return ApiResponse.onSuccess("초기 리그 순위 및 팀 통계 정보 등록 성공");
    }

    @PatchMapping("/football/update")
    public ApiResponse<?> updateStanding() throws JsonProcessingException {
        footballService.updateTeamStatisticsAndStanding();
        return ApiResponse.onSuccess("순위 및 통계 정보 최신화 성공");
    }

    @PatchMapping("/football/team/init")
    public ApiResponse<?> updateTeamInitInfo() {
        footballService.updateTeamInitInfo();
        return ApiResponse.onSuccess("팀 상세 정보 등록 성공");
    }

    @PatchMapping("/football/team/{teamId}")
    public ApiResponse<?> updateTeamInfo(@PathVariable Long teamId, @RequestBody TeamInfoUpdateDTO teamInfoUpdateDTO) {
        footballService.updateTeamInfo(teamId, teamInfoUpdateDTO);
        return ApiResponse.onSuccess("팀 정보 수정 완료");
    }

    @PatchMapping("/football/league")
    public ApiResponse<?> updateLeagueInfo(@RequestBody LeagueInfoUpdateDTO leagueInfoUpdateDTO) {
        footballService.updateLeagueInfo(leagueInfoUpdateDTO);
        return ApiResponse.onSuccess("리그 정보 수정 완료");
    }
}
