package com.flab.gettoticket.enums;

public enum BookingStatus {
    UPCOMING("upcoming", "다가오는 예매"),
    COMPLETED("completed", "완료된 예매"),
    CANCELLED("cancelled", "취소된 예매");

    private final String code;
    private final String description;

    BookingStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 문자열 -> enum
     * @param code "completed"
     * @return "COMPLETED"
     */
    public static BookingStatus setCode(String code) {
        for (BookingStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        return null;
    }
}
