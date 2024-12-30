package com.flab.gettoticket.common;

import lombok.Getter;

@Getter
public enum ApiResponseCode {
    SUCCESS(100, "요청이 성공하였습니다."),
    FAIL(99, "요청이 실패하였습니다."),

    BAD_REQUEST(400,"요청 파라미터가 올바르지 않습니다."),
    UNAUTHORIZED(401, "접근 권한이 없습니다."),
    FORBIDDEN(403,"허용되지 않은 접근입니다."),
    ERROR(500, "오류가 발생했습니다."),

    //예매
    BOOKING_SUCCESS(100, "예매에 성공하였습니다."),
    BOOKING_CANCLE_SUCCESS(100, "예매 취소가 완료되었습니다.");

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
