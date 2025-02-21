package com.example.demo.service.userService;

public interface UserService {

    public long getCurrentUID(String token);
    public boolean updateNickname(String username, String newNickname);
}
