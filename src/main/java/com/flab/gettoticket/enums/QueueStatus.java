package com.flab.gettoticket.enums;

public enum QueueStatus {
    // 대기 상태
    WAIT("wait", "대기중"),
    QUEUED("queued", "대기열 등록 완료"),
    EXPIRED("expired", "대기 시간 만료"),
    CANCELLED("cancelled", "대기 취소"),

    // 처리 상태
    PROCESS("process", "처리ㅃ중"),
    RESERVED("reserved", "좌석 임시 예약"),
    COMPLETED("completed", "처리 완료"),
    FAILED("failed", "처리 실패"),

    // 예외 상태
    TIMEOUT("timeout", "사용자 반응 없음"),
    ERROR("error", "시스템 오류");

    private final String code;
    private final String description;

    QueueStatus(String code, String description) {
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
