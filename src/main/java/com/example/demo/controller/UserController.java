package com.example.demo.controller;

import com.example.demo.dto.NicknameUpdateDTO;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.global.apipayLoad.code.ReasonDTO;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.apipayLoad.code.status.SuccessStatus;
import com.example.demo.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/user/update")
    public ApiResponse<String> updateNickname(@RequestBody NicknameUpdateDTO nicknameUpdateDTO) {
        // 🔥 현재 로그인된 사용자 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new TempHandler(ErrorStatus.SESSION_UNAUTHORIZED);
//        }

//        String username = authentication.getName(); // 현재 로그인된 사용자의 username 가져오기

        String username = nicknameUpdateDTO.getUsername();
        System.out.println(username);

        // 닉네임 변경
        String newToken = userService.updateNickname(username, nicknameUpdateDTO.getUserNickname());

        return ApiResponse.onSuccess(newToken);
    }

    // ✅ 기존 프로필 이미지를 업데이트 (덮어쓰기)
    @PutMapping("/user/profileIMG")
    public ApiResponse<Map<String,String>> updateProfileImage(@RequestParam String username, @RequestParam("file") MultipartFile file) {
        Map<String,String> newToken = userService.updateProfileImage(username, file);
        return ApiResponse.onSuccess(newToken);
    }
}
