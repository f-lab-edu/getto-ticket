package com.flab.gettoticket.enums;

public enum QueueStatus {
    // 대기 상태
    WAIT(100, "대기중"),
    QUEUED(101, "대기열 등록 완료"),
    EXPIRED(102, "대기 시간 만료"),
    CANCELLED(103, "대기 취소"),

    // 처리 상태
    PROCESS(200, "처리중"),
    RESERVED(201, "좌석 임시 예약"),
    COMPLETED(202, "처리 완료"),
    FAILED(203, "처리 실패"),

    // 예외 상태
    TIMEOUT(300, "사용자 반응 없음"),
    ERROR(301, "시스템 오류");

    private final int code;
    private final String description;

    QueueStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
