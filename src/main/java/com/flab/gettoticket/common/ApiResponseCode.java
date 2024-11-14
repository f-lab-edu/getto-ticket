package com.flab.gettoticket.common;

import lombok.Getter;

@Getter
public enum ApiResponseCode {
    SUCCESS(1, "요청이 성공하였습니다."),
    FAIL(0, "요청이 실패하였습니다."),

    BAD_REQUEST(40,"요청 파라미터가 올바르지 않습니다."),
    UNAUTHORIZED(41, "접근 권한이 없습니다."),
    FORBIDDEN(43,"허용되지 않은 접근입니다."),
    ERROR(50, "오류가 발생했습니다.");

    private final int code;
    private final String message;

    ApiResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
