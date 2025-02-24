package com.example.demo.controller;

import com.example.demo.domian.User;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.NewsResponseDto;
import com.example.demo.dto.GenerateNewsRequestDto;
import com.example.demo.dto.GenerateNewsResponseDto;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.exception.GeneralException;
import com.example.demo.service.NewsService.NewsService;
import com.example.demo.service.userService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/openai")
public class NewsController {

    private final NewsService newsService;
    private final UserService userService;

    @Operation(summary = "history 조회", description = "생성했던 기사 목록을 반환")
    @GetMapping("/history")
    public ApiResponse<List<NewsResponseDto>> getLatestFitNews(@AuthenticationPrincipal CustomUserDetails user){
        if (user == null) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }
        List<NewsResponseDto> latestNews = newsService.getLatestFitNews(userService.getCurrentUID(user.getUsername()));
        return ApiResponse.onSuccess(latestNews);
    }

    @Operation(summary = "history 삭제", description = "history 삭제")
    @DeleteMapping("/history/{historyId}")
    public ApiResponse<String> generateNewsResponse(@AuthenticationPrincipal CustomUserDetails user,
                                                                           @PathVariable(name = "historyId") Long historyId){
        if (user == null) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }

        newsService.deleteHistory(historyId, userService.getCurrentUID(user.getUsername()));
        return ApiResponse.onSuccess("history 삭제 성공");
    }

    @Operation(summary = "프롬프트 요청", description = "단어를 받아 OpenAI를 사용하여 스포츠 기사 생성")
    @PostMapping("/search")
    public ApiResponse<List<GenerateNewsResponseDto>> generateNewsResponse(@AuthenticationPrincipal CustomUserDetails user,
                                                                           @RequestBody GenerateNewsRequestDto requestDto){
        if (user == null) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }
        List<GenerateNewsResponseDto> newsResponse = newsService.generateNewsResponse(requestDto, userService.getCurrentUID(user.getUsername()));
        return ApiResponse.onSuccess(newsResponse);
    }


}
