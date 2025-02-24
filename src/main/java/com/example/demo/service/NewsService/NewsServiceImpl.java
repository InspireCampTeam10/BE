package com.example.demo.service.NewsService;

import com.example.demo.domian.News;
import com.example.demo.dto.NewsResponseDto;
import com.example.demo.dto.GenerateNewsRequestDto;
import com.example.demo.dto.GenerateNewsResponseDto;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.exception.GeneralException;
import com.example.demo.repository.NewsRepository;
import com.example.demo.service.OpenAIApiService.OpenAIApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final OpenAIApiService openAiApiService;


    public List<NewsResponseDto> getLatestFitNews(Long userId) {
        List<News> newsList = newsRepository.findTop6ByUserIdOrderByTimestampDesc(userId)
                .orElse(Collections.emptyList());

        if (newsList.isEmpty()) {
            throw new GeneralException(ErrorStatus.NEWS_NOT_FOUND);
        }

        return newsList.stream()
                .map(news -> new NewsResponseDto(news.getId(), news.getTitle(), news.getContent(), news.getTimestamp()))
                .collect(Collectors.toList());
    }


    public List<GenerateNewsResponseDto> generateNewsResponse(GenerateNewsRequestDto requestDto, Long userId) {
        List<GenerateNewsResponseDto> newsResponse = openAiApiService.generateNewsResponse(requestDto);

        List<News> histories = newsResponse.stream()
                        .flatMap(dto -> dto.getNews().stream())
                        .map(newsItem -> News.builder()
                                .userId(userId)
                                .title(newsItem.getTitle())
                                .content(newsItem.getContent())
                                .build())
                        .collect(Collectors.toList());
        newsRepository.saveAll(histories);
        return newsResponse;
    };


    public void deleteHistory(Long historyId, Long userId){
        News history = newsRepository.findById(historyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NEWS_NOT_FOUND));

        if (!history.getUserId().equals(userId)) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }

        newsRepository.delete(history);
    }

    // 애플리케이션 실행 시 더미 데이터 삽입
    /*
    @PostConstruct
    public void initializeTestData() {
        List<History> testData = Arrays.asList(
                new History(null, 1L, "User 1 - News 1", "Summary 1", LocalDateTime.now().minusMinutes(10)),
                new History(null, 2L, "User 2 - News 1", "Summary 2", LocalDateTime.now().minusMinutes(9)),
                new History(null, 1L, "User 1 - News 2", "Summary 3", LocalDateTime.now().minusMinutes(8)),
                new History(null, 2L, "User 2 - News 2", "Summary 4", LocalDateTime.now().minusMinutes(7)),
                new History(null, 1L, "User 1 - News 3", "Summary 5", LocalDateTime.now().minusMinutes(6)),
                new History(null, 2L, "User 2 - News 3", "Summary 6", LocalDateTime.now().minusMinutes(5)),
                new History(null, 1L, "User 1 - News 4", "Summary 7", LocalDateTime.now().minusMinutes(4)),
                new History(null, 2L, "User 2 - News 4", "Summary 8", LocalDateTime.now().minusMinutes(3)),
                new History(null, 1L, "User 1 - News 5", "Summary 9", LocalDateTime.now().minusMinutes(2)),
                new History(null, 2L, "User 2 - News 5", "Summary 10",LocalDateTime.now().minusMinutes(1))
        );

        historyRepository.saveAll(testData);
    }
    */
}
