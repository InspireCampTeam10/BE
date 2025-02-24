package com.example.demo.controller;

import com.example.demo.JWT.JWTUtil;
import com.example.demo.dto.joinDTO;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.global.apipayLoad.code.ReasonDTO;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.apipayLoad.code.status.SuccessStatus;
import com.example.demo.service.userService.JoinService;
import com.example.demo.service.userService.JoinServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class JoinController {

    private final JoinService joinService;
    private final JWTUtil jwtUtil;

    public JoinController(JoinService joinService, JWTUtil jwtUtil) {
        this.joinService = joinService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/user/join")
    public ApiResponse<Map<String, Object>> joinProcess(@RequestBody joinDTO joinDTO) {
            joinService.joinProcess(joinDTO);
            //회원가입 성공 시 JWT 토큰 생성
            String token = jwtUtil.createJwt(joinDTO.getUsername(), "ROLE_USER", joinDTO.getUserNickname(), null,6000 * 6000 * 100L);

            // 응답 데이터 생성 (토큰 포함)
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("isSuccess", true);
            responseBody.put("token", token);
            responseBody.put("username", joinDTO.getUsername());

            //성공 응답 (201 Created)
            return ApiResponse.onSuccess(responseBody);
    }
}
