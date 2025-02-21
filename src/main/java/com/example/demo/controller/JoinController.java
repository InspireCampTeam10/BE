package com.example.demo.controller;

import com.example.demo.dto.joinDTO;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.global.apipayLoad.code.ReasonDTO;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.apipayLoad.code.status.SuccessStatus;
import com.example.demo.global.apipayLoad.handler.TempHandler;
import com.example.demo.service.userService.JoinService;
import com.example.demo.service.userService.JoinServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/user/join")
    public ResponseEntity<ApiResponse<Boolean>> joinProcess(joinDTO joinDTO) {
        boolean isSuccess = joinService.joinProcess(joinDTO);

        if (isSuccess) {
            //  회원가입 성공 (201 or 200 Created)
            return ResponseEntity
                    .ok(ApiResponse.onSuccess(true));
        } else {
            //  회원가입 실패 (이미 존재하는 회원)
            //  throw new TempHandler(ErrorStatus.MEMBER_ALREADY_EXIST);
            return ResponseEntity
                    .status(400)
                    .body(ApiResponse.onFailure(ErrorStatus.MEMBER_ALREADY_EXIST.getCode(),
                            ErrorStatus.MEMBER_ALREADY_EXIST.getMessage(), false));
        }
    }
}
