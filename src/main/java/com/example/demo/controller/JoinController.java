package com.example.demo.controller;

import com.example.demo.JWT.JWTUtil;
import com.example.demo.dto.joinDTO;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.global.apipayLoad.code.ReasonDTO;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.apipayLoad.code.status.SuccessStatus;
import com.example.demo.global.apipayLoad.handler.TempHandler;
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
    public ResponseEntity<ApiResponse<Map<String, Object>>>joinProcess(@RequestBody joinDTO joinDTO) {
        boolean isSuccess = joinService.joinProcess(joinDTO);

        if (isSuccess) {
            //회원가입 성공 시 JWT 토큰 생성
            String token = jwtUtil.createJwt(joinDTO.getUsername(), "ROLE_USER", 6000 * 6000 * 100L);

            // 응답 데이터 생성 (토큰 포함)
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("isSuccess", true);
            responseBody.put("token", token);
            responseBody.put("username", joinDTO.getUsername());

            //성공 응답 (201 Created)
            return ResponseEntity
                    .status(201)
                    .body(ApiResponse.onSuccess(responseBody));
        } else {
            //회원가입 실패 (이미 존재하는 회원)
            return ResponseEntity
                    .status(400)
                    .body(ApiResponse.onFailure(ErrorStatus.MEMBER_ALREADY_EXIST.getCode(),
                            ErrorStatus.MEMBER_ALREADY_EXIST.getMessage(), null));
        }
    }
}
