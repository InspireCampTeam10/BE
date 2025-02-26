package com.example.demo.JWT;

import com.example.demo.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/user/login"); // 로그인 URL 추가
    }

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//
//        System.out.println(username);
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
//
//        return authenticationManager.authenticate(authToken);
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("attemptAuthentication + Debug");
        // JSON 형식으로 Request가 들어가서 ObjectMapper로 아이디 비밀번호 추출
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonRequest = null;

        try {
            jsonRequest = mapper.readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = jsonRequest.get("username");
        String password = jsonRequest.get("password");

        System.out.println("username, password in attemptAuthentic: " + username + " " + password);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        String nickname = customUserDetails.getUserNickname();
        String imgUrl = customUserDetails.getUserImgUrl(); // DB에 저장된 프로필 이미지 URL

        // Base64 변환할 변수 선언
        String base64Image = null;

        if (imgUrl != null && !imgUrl.isEmpty()) {
            try {
                Path imagePath = Paths.get("uploads/" + imgUrl.substring(imgUrl.lastIndexOf("/") + 1));
                if (Files.exists(imagePath)) {
                    byte[] fileBytes = Files.readAllBytes(imagePath);
                    base64Image = Base64.getEncoder().encodeToString(fileBytes);
                }
            } catch (IOException e) {
                base64Image = null; // 이미지 읽기 실패 시 null 처리
            }
        }

        // JWT 생성 (이미지 URL 포함)
        String token = jwtUtil.createJwt(username, role, nickname, imgUrl, 600 * 600 * 100L);

        // 응답 JSON 구성
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("image", base64Image); // Base64 이미지 추가 (없으면 null)

        // 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답 반환
        new ObjectMapper().writeValue(response.getWriter(), responseBody);
    }



    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
    throws IOException {
        // System.out.println("Unsuccessful Authentication");
        // response.setStatus(401);

        // 응답 상태 코드 설정 (401 Unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답을 위한 Map 생성
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isSuccess", false);
        responseBody.put("httpStatus", "UNAUTHORIZED");
        responseBody.put("code", "AUTH401");
        responseBody.put("message", "로그인 실패: 잘못된 사용자 정보입니다.");
        responseBody.put("error", failed.getMessage());  // 실제 예외 메시지도 포함 가능

        // Jackson ObjectMapper를 사용하여 JSON 변환 후 응답 Body에 추가
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseBody);

    }
}
