package com.example.demo.controller;

import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.service.testService.TestService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @Operation(summary = "성공 테스트", description = "성공 테스트")
    @PostMapping("/success")
    public ApiResponse<String> success_test(){
        testService.success_test();
        return ApiResponse.onSuccess("성공");
    }

    @Operation(summary = "에러 테스트", description = "에러 테스트")
    @GetMapping("/error")
    public ApiResponse<String> error_test(){
        testService.error_test();
        return ApiResponse.onSuccess("성공");
    }

}
