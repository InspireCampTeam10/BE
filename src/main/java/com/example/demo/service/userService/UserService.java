package com.example.demo.service.userService;

import com.example.demo.global.apipayLoad.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {

    public ResponseEntity<ApiResponse<Long>>  getCurrentUID(String userName);
    public boolean updateNickname(String username, String newNickname);
}
