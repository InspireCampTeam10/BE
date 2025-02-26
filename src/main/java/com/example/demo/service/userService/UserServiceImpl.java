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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    // Base64 인코딩 메서드 추가
    private String encodeFileToBase64(Path filePath) throws IOException {
        byte[] fileBytes = Files.readAllBytes(filePath);
        return Base64.getEncoder().encodeToString(fileBytes);
    }

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
    public String updateNickname(String username, String newNickname) {
        User userEntity = userRepository.findByUsername(username);

        System.out.println("username: " + username + ", newNickName: " + newNickname);
        if (userEntity == null) {
            System.out.println("사용자 없음");
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }

        // 닉네임 변경
        userEntity.setUserNickname(newNickname);
        userRepository.save(userEntity);

        // 새로운 JWT 발급 (변경된 닉네임 포함)
        String newToken = jwtUtil.createJwt(userEntity.getUsername(), userEntity.getRole(), userEntity.getUserNickname(), userEntity.getProfileImageUrl(),600 * 600 * 100L);

        return newToken;  //새로운 JWT 반환
    }


    @Transactional
    @Override
    public Map<String, String> updateProfileImage(String username, MultipartFile file) {
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
            String fileUrl = "/uploads/" + fileName;

            // User 엔티티에 저장
            userEntity.setProfileImageUrl(fileUrl);
            userRepository.save(userEntity);

            // Base64 인코딩
            String base64Image = encodeFileToBase64(filePath);

            // 새로운 JWT 생성
            String newToken = jwtUtil.createJwt(userEntity.getUsername(), userEntity.getRole(), userEntity.getUserNickname(), fileUrl, 600 * 600 * 100L);

            // JSON 형태로 반환
            Map<String, String> response = new HashMap<>();
            response.put("token", newToken);
            response.put("image", base64Image);

            return response;
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED);
        }
    }
}
