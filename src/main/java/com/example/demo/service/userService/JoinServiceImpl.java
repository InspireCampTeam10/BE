package com.example.demo.service.userService;

import com.example.demo.domian.User;
import com.example.demo.dto.joinDTO;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinServiceImpl implements JoinService {


    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(joinDTO joinDTO){
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String userNickname = joinDTO.getUserNickname();

        // 존재하는지 확인하고 결과값이 isExist에 존재
        Boolean isExist = userRepository.existsByUsername(username);

        System.out.println("username : " + username + " password : " + password + " nickname : " + userNickname);
        System.out.println("isExist : " + isExist);
        if(isExist){

            return;
        }

        User data = new User();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setUserNickname(userNickname);
        data.setRole("ROLE_USER");

        userRepository.save(data);
    }
}
