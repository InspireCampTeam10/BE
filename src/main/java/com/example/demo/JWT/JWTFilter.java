package com.example.demo.JWT;

import com.example.demo.domian.User;
import com.example.demo.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    // 요청에 의해 한 번만 작동하는 -> OncePerRequestFilter

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");
        System.out.println("authorization" + authorization);
        // Authorization 헤더 검증
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);

            // 조건이 해당되면 메소드 종료(필수)

            return;
        }

        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰 소멸 시간 검증
        if(jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            // 조건이 해당되면, 메소드 종료(필수)
            return;
        }

        // 토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //User 엔티티를 생성하여 값을 set 해줌
        User userEntity = new User();
        userEntity.setUsername(username);
        userEntity.setRole(role);
        // 비밀번호 값은 토큰에 담겨있지 않다. 그렇지만 초기화는 해줘야 한다. -> 임시적으로 강제로 만들어서 초기화시켜줌
        userEntity.setPassword("temppassword");

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 유저 세션을 생성
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 우리 필터가 끝나면, 다음 필터에게 우리가 받았던 request와 response를 던저주는 거임 -> 그게 doFilter
        filterChain.doFilter(request, response);

    }
}
