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
    public long getCurrentUID(String userName) {

        String username = userRepository.findByUsername(userName).getUsername();

        //userName에 해당하는 정보가 존재하는지 확인
        if ( username == null) {
            throw new IllegalStateException("JWT에서 UID를 찾을 수 없습니다.");
        }


        User userEntity = userRepository.findByUsername(username);
        long uid = userEntity.getUid();

        System.out.println("uid: " + uid);
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
