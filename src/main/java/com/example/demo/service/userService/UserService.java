package com.example.demo.service.userService;

import com.example.demo.global.apipayLoad.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {

    public Long  getCurrentUID(String userName);
    public void updateNickname(String username, String newNickname);
    public String updateProfileImage(String username, MultipartFile file);
}
