package com.flab.gettoticket.enums;

public enum SeatStatus {
    FOR_SALE("N", "판매 가능"),
    SOLD_OUT("Y", "판매 완료");

    private final String code;
    private final String description;

    SeatStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
