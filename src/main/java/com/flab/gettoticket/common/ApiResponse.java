package com.flab.gettoticket.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(ApiResponseCode.SUCCESS.getCode(), ApiResponseCode.SUCCESS.getMessage(), data);
    }
    public static <T> ApiResponse<T> fail(T data) {
        return new ApiResponse<>(ApiResponseCode.FAIL.getCode(), ApiResponseCode.FAIL.getMessage(), data);
    }
    public static <T> ApiResponse<T> create(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> create(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
}
