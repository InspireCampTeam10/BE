package com.example.demo.global.exception;

import com.example.demo.global.apipayLoad.code.ReasonDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    // throw한 GeneralException에 대한 예외 처리
    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<?> onThrowException(GeneralException generalException, HttpServletRequest request) {
        ReasonDTO errorReasonHttpStatus = generalException.getErrorStatus();
        return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
    }

    // 필수 요청 데이터(RequestPart)가 누락된 경우에 대한 예외 처리
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ReasonDTO errorReason = ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("필수 요청 데이터가 누락되었습니다: " + e.getRequestPartName())
                .code("MISSING_REQUEST_PART")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorReason);
    }

    // Content-Type이 multipart/form-data가 아닌 경우에 대한 예외 처리
    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<?> handleInvalidContentTypeException(InvalidContentTypeException e, HttpServletRequest request) {
        ReasonDTO errorReason = ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("요청의 Content-Type이 올바르지 않습니다. multipart/form-data 형식이어야 합니다.")
                .code("INVALID_CONTENT_TYPE")
                .build();

        return handleExceptionInternal(e, errorReason, null, request);
    }

    // IllegalArgumentException 에 대한 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        ReasonDTO errorReasonHttpStatus = ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .code("ARGUMENT_ERROR")
                .build();
        return handleExceptionInternal(e, errorReasonHttpStatus, null, request);
    }

    // DTO Validation Error 에 대한 예외 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ReasonDTO errorReasonHttpStatus = ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .code("VALIDATION_ERROR")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorReasonHttpStatus);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ReasonDTO reason, HttpHeaders headers, HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        return handleExceptionInternal(e, reason, headers, reason.getHttpStatus(), webRequest);
    }

}