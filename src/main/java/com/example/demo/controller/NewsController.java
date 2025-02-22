package com.example.demo.controller;

import com.example.demo.dto.NewsResponseDto;
import com.example.demo.dto.GenerateNewsRequestDto;
import com.example.demo.dto.GenerateNewsResponseDto;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.service.NewsService.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/openai")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "history 조회", description = "생성했던 기사 목록을 반환")
    @GetMapping("/history")
    public ApiResponse<List<NewsResponseDto>> getLatestFitNews(){
        // UserID 조회 추가 + 예외 처리
        Long userId = 1L;

        List<NewsResponseDto> latestNews = newsService.getLatestFitNews(userId);
        return ApiResponse.onSuccess(latestNews);
    }

    @Operation(summary = "프롬프트 요청", description = "단어를 받아 OpenAI를 사용하여 스포츠 기사 생성")
    @PostMapping("/search")
    public ApiResponse<List<GenerateNewsResponseDto>> generateNewsResponse(@RequestBody GenerateNewsRequestDto requestDto){
        // UserID 조회 추가 + 예외 처리
        Long userId = 1L;

        List<GenerateNewsResponseDto> newsResponse = newsService.generateNewsResponse(requestDto, userId);
        return ApiResponse.onSuccess(newsResponse);
    }
}
