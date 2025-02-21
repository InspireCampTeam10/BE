package com.example.demo.service.userService;

import com.example.demo.JWT.JWTUtil;
import com.example.demo.domian.User;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.apipayLoad.handler.TempHandler;
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


    @Override
    public ResponseEntity<ApiResponse<Long>> getCurrentUID(String username) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .status(400)  // 🔥 HTTP 상태 코드 400 설정
                    .body(ApiResponse.onFailure(ErrorStatus.MEMBER_NOT_FOUND.getCode(),
                            ErrorStatus.MEMBER_NOT_FOUND.getMessage(), null));
        }

        Long uid = userOptional.get().getUid();
        return ResponseEntity
                .ok(ApiResponse.onSuccess(uid)); // ✅ 성공 시 200 OK 반환
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
