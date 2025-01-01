package com.flab.gettoticket.enums;

public enum SeatStatus {
    FOR_SALE(100, "판매 가능"),
    RESERVED(200, "판매 예약"),
    SOLD_OUT(300, "판매 완료");

    private final int code;
    private final String description;

    SeatStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() { return code; }
    public String getDescription() {
        return description;
    }

}
