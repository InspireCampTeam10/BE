package com.example.demo.service.OpenAIApiService;


import com.example.demo.dto.GenerateNewsRequestDto;
import com.example.demo.dto.GenerateNewsResponseDto;
import com.example.demo.dto.NewsItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenAIApiServiceImpl implements OpenAIApiService {

    @Value("${openai.api-key}")
    private String API_KEY;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String GPT_MODEL = "gpt-3.5-turbo";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<GenerateNewsResponseDto> generateNewsResponse(GenerateNewsRequestDto requestDto) {
        Map<String, String> systemMessage = getSystemMessage();

        Map response = getOpenAIResponse(requestDto, systemMessage);

        return parseOpenAIResponse(response);
    }

    private List<GenerateNewsResponseDto> parseOpenAIResponse(Map response) {
        String assistantContent = extractAssistantContent(response);
        if (assistantContent == null) {
            return Collections.emptyList();
        }

        // JSON 파싱: assistantContent를 GenerateNewsResponseDto 형태로 역직렬화
        //   - 응답 자체가 {"news": [...]} 형태
        List<GenerateNewsResponseDto> resultList = new ArrayList<>();
        try {
            // (1) JSON 문자열 파싱
            JsonNode rootNode = objectMapper.readTree(assistantContent);

            // (2) news 배열 추출
            JsonNode newsArray = rootNode.get("news");
            if (newsArray == null || !newsArray.isArray()) {
                return Collections.emptyList();
            }

            // (3) NewsItem 리스트 만들기
            List<NewsItem> newsItems = new ArrayList<>();
            for (JsonNode item : newsArray) {
                String title = item.get("title").asText("");
                String content = item.get("content").asText("");
                newsItems.add(new NewsItem(title, content));
            }

            // (4) 하나의 GenerateNewsResponseDto에 news 리스트를 담아서 반환
            GenerateNewsResponseDto dto = new GenerateNewsResponseDto(newsItems);
            resultList.add(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return resultList;
    }

    private Map getOpenAIResponse(GenerateNewsRequestDto requestDto, Map<String, String> systemMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", createUserPrompt(requestDto));

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(userMessage);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", GPT_MODEL);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 500);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        Map response = restTemplate.postForObject(API_URL, entity, Map.class);
        System.out.println("OpenAI Response: " + response);
        return response;
    }

    private static Map<String, String> getSystemMessage() {
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content",
                "당신은 뉴스 생성 AI입니다. " +
                        "아래 JSON 포맷을 반드시 준수해서 답변해주세요. " +
                        "절대 JSON 외의 다른 텍스트를 포함하지 마세요.\n\n" +
                        "응답 예시(JSON):\n" +
                        "{\n" +
                        "  \"news\": [\n" +
                        "    {\n" +
                        "      \"title\": \"뉴스 제목\",\n" +
                        "      \"content\": \"뉴스 내용\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"
        );
        return systemMessage;
    }

    // 사용자 프롬프트 메시지 생성
    private String createUserPrompt(GenerateNewsRequestDto requestDto) {
        return String.format(
                "키워드는 %s 입니다. " +
                        "뉴스의 내용은 7문장으로 해주세요.",
                requestDto.getKeywords().toString()
        );
    }


    // ChatCompletion API 응답 중에서 "assistant"의 content를 추출
    private String extractAssistantContent(Map response) {
        if (response == null) return null;
        List<Map> choices = (List<Map>) response.get("choices");
        if (choices == null || choices.isEmpty()) return null;

        Map choice = choices.get(0);
        Map messageObj = (Map) choice.get("message");
        if (messageObj == null) return null;

        return (String) messageObj.get("content");
    }
}