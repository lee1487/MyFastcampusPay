package com.fastcampuspay.common.CnEnum;

// "success", "fail", "ready"
public enum SubTaskStatus {
    SUCCESS("success"),     // 성공
    FAIL("fail"),           // 실패
    READY("ready");         // 대기

    private final String value;

    SubTaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SubTaskStatus fromValue(String value) {
        for (SubTaskStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid SubTaskStatus value: " + value);
    }
}
