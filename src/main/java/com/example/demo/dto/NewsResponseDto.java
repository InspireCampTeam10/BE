package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NewsResponseDto {
    private String title;
    private String summary;
    private LocalDateTime timestamp;
}
