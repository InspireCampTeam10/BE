package com.example.demo.service.userService;

import com.example.demo.JWT.JWTUtil;
import com.example.demo.domian.User;
import com.example.demo.global.apipayLoad.ApiResponse;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import com.example.demo.global.exception.GeneralException;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public UserServiceImpl(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    // Return -> Long
    @Override
    public Long getCurrentUID(String username) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        if (userOptional.isEmpty()) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        Long uid = userOptional.get().getUid();
        return uid;
    }

    @Transactional
    @Override
    public void updateNickname(String username, String newNickname){
        User userEntity = userRepository.findByUsername(username);

        System.out.println("username: " + username + ", newNickName: " + newNickname);
        if(userEntity == null){
            System.out.println("사용자 없음");
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        userEntity.setUserNickname(newNickname);
        userRepository.save(userEntity);
    }

    // 프로필 이미지 업로드 및 저장 (로컬에 저장)
    @Transactional
    @Override
    public String updateProfileImage(String username, MultipartFile file) {
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        try {
            // 저장할 디렉토리 설정
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));

            // 파일 저장
            String fileName = username + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());

            // 저장된 파일의 URL 생성
            String fileUrl = "/uploads/" + fileName;  // 로컬 서버에서 접근 가능하게 설정

            // User 엔티티에 저장
            userEntity.setProfileImageUrl(fileUrl);
            userRepository.save(userEntity);

            return fileUrl;
        } catch (Exception e) {
//            throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED);
        }
    }
}
