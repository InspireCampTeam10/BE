package com.example.demo.controller;

import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.service.teamService.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/home")
public class HomeController {

    private final TeamService teamService;

    @GetMapping("/")
    public ApiResponse<?> getTeam(){
        return ApiResponse.onSuccess("성공");
    }
}
