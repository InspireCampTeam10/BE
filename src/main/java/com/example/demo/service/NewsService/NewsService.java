package com.example.demo.service.NewsService;

import com.example.demo.dto.NewsResponseDto;
import com.example.demo.dto.GenerateNewsRequestDto;
import com.example.demo.dto.GenerateNewsResponseDto;

import java.util.List;

public interface NewsService {
    List<NewsResponseDto> getLatestFitNews(Long userId);

    List<GenerateNewsResponseDto> generateNewsResponse(GenerateNewsRequestDto requestDto, Long userId);

    void deleteHistory(Long userId, Long historyId);
}
