package com.example.demo.service.footballService;

import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.exception.GeneralException;
import com.example.demo.repository.LeagueRepository;
import com.example.demo.repository.StandingRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.TeamStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FootballServiceImpl implements FootballService {

    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final StandingRepository standingRepository;
    private final TeamStatisticsRepository teamStatisticsRepository;

    @Value("${api.key}")
    private String API_KEY;
    @Value("${api.url}")
    private String REQUEST_URL;


    @Override
    public void registerOrUpdateTeam() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-rapidapi-key", API_KEY);

        ResponseEntity<String> response;
        RestTemplate restTemplate = new RestTemplate();

        try {
            // RapidAPI 서버에 GET 요청을 보내 이미지 생성
            log.info("2024 EPL 정보 요청 시작");
            response = restTemplate.getForEntity(REQUEST_URL, String.class);
            log.info("2024 EPL 정보 요청 완료");
        } catch (Exception e) {
            log.error("Failed to call RapidAPI: {}", e.getMessage());
            throw new GeneralException(ErrorStatus.BAD_RAPID_API_REQUEST);
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println(response.getBody());
        } else {
            log.error("Failed to call RapidAPI: {}", response.getStatusCode());
        }
        String responseBody = response.getBody();
        if(responseBody.isEmpty()) {
            log.error("RapidAPI responseBody is empty");
        }
        else{
            System.out.println("responseBody = " + responseBody);

        }
    }
}