package com.example.demo.global.apipayLoad.code;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"isSuccess", "httpStatus", "code", "message"})
public class ReasonDTO {
    private final Boolean isSuccess;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Builder
    public ReasonDTO(HttpStatus httpStatus, Boolean isSuccess, String code, String message) {
        this.httpStatus = httpStatus;
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
