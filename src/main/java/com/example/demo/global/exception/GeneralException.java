package com.example.demo.global.exception;


import com.example.demo.global.apipayLoad.code.ReasonDTO;
import com.example.demo.global.apipayLoad.code.status.ErrorStatus;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public GeneralException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ReasonDTO getErrorStatus() {
        return this.errorStatus.getReason();
    }
}