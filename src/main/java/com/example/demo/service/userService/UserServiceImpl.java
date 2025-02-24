package com.example.demo.service.userService;

import com.example.demo.JWT.JWTUtil;
import com.example.demo.domian.User;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.apipayLoad.handler.TempHandler;
import com.example.demo.global.exception.GeneralException;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public UserServiceImpl(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    // Return -> Long
    @Override
    public Long getCurrentUID(String username) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        if (userOptional.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Long uid = userOptional.get().getUid();
        return uid;
    }

    @Transactional
    @Override
    public void updateNickname(String username, String newNickname){
        User userEntity = userRepository.findByUsername(username);

        System.out.println("username: " + username + ", newNickName: " + newNickname);
        if(userEntity == null){
            System.out.println("사용자 없음");
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        userEntity.setUserNickname(newNickname);
        userRepository.save(userEntity);
    }
}
