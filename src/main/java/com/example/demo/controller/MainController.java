package com.example.demo.controller;

import com.example.demo.service.userService.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Iterator;

@Controller
@ResponseBody
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String mainP(){

        // 현재 세션의 사용자 아이디
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 현재 세션 사용자의 Role 값
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();


        return "Main Controller(Username: " + username + " | Role: " + role + ")";
    }


    @GetMapping("/user/uid")
    public Long getClaims(@RequestParam String token){
        return userService.getCurrentUID(token);
    }
}
