package com.example.demo.global.apipayLoad.code.status;

import com.example.demo.global.apipayLoad.code.BaseCode;
import com.example.demo.global.apipayLoad.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "회원이 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "MEMBER4002", "로그인 과정에서 오류가 발생했습니다."),
    SESSION_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "MEMBER4003", "유효하지 않은 세션입니다."),
    AUTH_REQUEST_BODY_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4004", "Authentication Request Body를 읽지 못했습니다."),
    USERNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4005", "유저 아이디가 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4006", "유저 이메일이 없습니다."),
    PASSWORD_NOT_FUND(HttpStatus.BAD_REQUEST, "MEMBER4007", "유저 비밀번호가 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST,"MEMBER4008","재입력한 비밀번호와 일치하지 않습니다."),
    PASSWORD_NOT_INVALID(HttpStatus.BAD_REQUEST,"MEMBER4009","유효하지않은 비밀번호입니다"),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4010", "이미 존재하는 아이디입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER4011", "접근 권한이 없습니다."),
    PROVIDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4012", "일치하는 Provider가 없습니다."),
    ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4013", "일치하는 Role 없습니다."),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4 014", "삭제할 프로필 이미지가 없습니다"),

    // Token Error
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN4001", "유효하지 않은 엑세스 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN4001", "리프레쉬 토큰이 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN4001", "리프레쉬 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN4001", "유효하지 않은 리프레쉬 토큰입니다."),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED,"ACCESS_TOKEN4001","엑세스 토큰이 없습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN4002", "엑세스 토큰이 만료되었습니다."),

    // League Error
    LEAGUE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "LEAGUE4001", "이미 존재하는 리그입니다."),
    INIT_INFO_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "LEAGUE4002", "이미 초기 정보가 등록된 리그입니다."),
    LEAGUE_NOT_FOUND(HttpStatus.BAD_REQUEST, "LEAGUE4003", "리그가 존재하지 않습니다."),
    INIT_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "LEAGUE4004", "초기 정보가 존재하지 않습니다."),

    // Upload Error
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_UPLOAD5001", "파일 업로드에 실패했습니다."),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_UPLOAD5002", "파일 삭제에 실패했습니다."),

    //method Error
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,"METHOD405", "허용되지 않은 HTTP 메서드입니다."),

    // request Error
    BAD_RAPID_API_REQUEST(HttpStatus.BAD_REQUEST, "REQUEST4001", "RapidAPI 요청에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}