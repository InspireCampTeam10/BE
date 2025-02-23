package com.example.demo.controller;

import com.example.demo.dto.NicknameUpdateDTO;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.global.apipayLoad.code.ReasonDTO;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.apipayLoad.code.status.SuccessStatus;
import com.example.demo.global.apipayLoad.handler.TempHandler;
import com.example.demo.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/user/update")
    public ResponseEntity<ApiResponse<Boolean>> updateNickname(NicknameUpdateDTO nicknameUpdateDTO) {
        // 🔥 현재 로그인된 사용자 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new TempHandler(ErrorStatus.SESSION_UNAUTHORIZED);
//        }

//        String username = authentication.getName(); // 현재 로그인된 사용자의 username 가져오기

        String username = nicknameUpdateDTO.getUsername();
        System.out.println(username);

        // 🔥 닉네임 변경
        boolean isUpdated = userService.updateNickname(username, nicknameUpdateDTO.getUserNickname());

//        if (isUpdated) {
//            // ✅ 닉네임 변경 성공 응답 반환
//            return ResponseEntity.
//                    status(SuccessStatus.OK.getHttpStatus())
//                    .body(SuccessStatus.OK.getReason());
//        }else{
//            throw new TempHandler(ErrorStatus.MEMBER_NOT_FOUND);
//        }

        if(isUpdated) {
            return ResponseEntity
                    .ok(ApiResponse.onSuccess(true));
        }else{
            return  ResponseEntity
                    .status(400)
                    .body(ApiResponse.onFailure(ErrorStatus.MEMBER_NOT_FOUND.getCode(), ErrorStatus.MEMBER_NOT_FOUND.getMessage(), false));

        }
    }
}
