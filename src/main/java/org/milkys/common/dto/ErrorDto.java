package org.milkys.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorDto {
    private final String message;
    private final int code;

    /**
     * 목적: Throwable 예외 객체와 HttpStatus 상태 코드를 사용하여 ErrorDto를 생성합니다.
     * 동작: throwable의 메시지와 status의 상태 코드를 사용하여 ErrorDto 객체를 생성합니다.
     */
    public static ErrorDto of(Throwable throwable, HttpStatus status) {
        return new ErrorDto(throwable, status);
    }
    private ErrorDto(Throwable throwable, HttpStatus status) {
        this(throwable.getMessage(),status);
    }
    private ErrorDto(String errorMessage, HttpStatus status) {
        this.message = errorMessage;
        this.code = status.value();
    }
    public static ErrorDto of(String errorMessage, HttpStatus status) {
        return new ErrorDto(errorMessage,status);
    }
    private ErrorDto(String errorMessage, int errorCode) {
        this.message = errorMessage;
        this.code = errorCode;
    }
    public static ErrorDto of(String errorMessage, int errorCode) {
        return new ErrorDto(errorMessage, errorCode);
    }
}
