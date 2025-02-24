package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GenerateNewsRequestDto {
    private List<String> keywords;  // OpenAI에 전달할 키워드 리스트
}