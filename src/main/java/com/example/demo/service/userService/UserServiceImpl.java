package com.example.demo.service.userService;

import com.example.demo.JWT.JWTUtil;
import com.example.demo.domian.User;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.beans.Transient;

@Service
public class UserServiceImpl implements UserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public UserServiceImpl(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    @Override
    public long getCurrentUID(String token) {
        // JWT에서 모든 Claims 가져오기
        Claims claims = jwtUtil.extractClaims(token);

        //UID가 존재하는지 확인 후 long 타입으로 변환
        if (claims.get("username") == null) {
            throw new IllegalStateException("JWT에서 UID를 찾을 수 없습니다.");
        }

        System.out.println(claims);


        User userEntity = userRepository.findByUsername((String) claims.get("username"));
        long uid = userEntity.getUid();

        System.out.println(uid);
        return uid; // long 타입으로 변환 후 반환

    }

    @Transactional
    @Override
    public boolean updateNickname(String username, String newNickname){
        User userEntity = userRepository.findByUsername(username);

        System.out.println("username: " + username + ", newNickName: " + newNickname);
        if(userEntity == null){
            System.out.println("사용자 없음");
            return false;
        }

        userEntity.setUserNickname(newNickname);
        userRepository.save(userEntity);
        return true;
    }
}
