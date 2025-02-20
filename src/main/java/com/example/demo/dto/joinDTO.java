package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class joinDTO {

    // 회원가입 받을 때 사용하는 DTO
    private String username;

    private String password;

    private String userNickname;

}
