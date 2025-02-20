package com.example.demo.controller;

import com.example.demo.dto.joinDTO;
import com.example.demo.service.userService.JoinService;
import com.example.demo.service.userService.JoinServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinProcess(joinDTO joinDTO) {

        joinService.joinProcess(joinDTO);
        System.out.println("Test Init");

        return "ok";
    }
}
