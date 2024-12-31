package com.flab.gettoticket.enums;

public enum BookingStatus {
    UPCOMING(100, "다가오는 예매"),
    COMPLETED(200, "완료된 예매"),
    CANCELLED(300, "취소된 예매");

    private final int code;
    private final String description;

    BookingStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 정수 code -> enum
     * @param code 100, 200..
     * @return "COMPLETED"
     */
    public static BookingStatus setCode(int code) {
        for (BookingStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
