package com.example.demo.service.OpenAIApiService;

import com.example.demo.dto.GenerateNewsRequestDto;
import com.example.demo.dto.GenerateNewsResponseDto;

import java.util.List;

public interface OpenAIApiService {
    List<GenerateNewsResponseDto> generateNewsResponse(GenerateNewsRequestDto requestDto);
}
