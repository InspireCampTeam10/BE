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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        System.out.println(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
    throws IOException {
//        System.out.println("Successful Authentication");
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username,role,600*600*100L);

        // 베어러 텍스트 뒤에 띄어쓰기 꼭 해야한다 ㅋㅋ
        response.addHeader("Authorization","Bearer "+token);

        //  JSON 형식의 응답을 만들기 위해 Map 사용
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("token_type", "Bearer");
        responseBody.put("username", username);
        responseBody.put("role", role);
        // 여기서 UserNickname 추가할수도있음 ㅇㅇ

        // 응답을 JSON 형식으로 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Jackson의 ObjectMapper를 사용하여 JSON으로 변환 후 응답 Body에 추가
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
