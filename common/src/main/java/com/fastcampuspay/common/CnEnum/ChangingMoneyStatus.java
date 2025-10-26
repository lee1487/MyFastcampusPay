package com.fastcampuspay.common.CnEnum;

public enum ChangingMoneyStatus {
    REQUESTED(0),  // 요청됨
    SUCCEEDED(1),  // 성공
    FAILED(2),     // 실패
    CANCELED(3);   // 취소됨

    private final int value;

    ChangingMoneyStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ChangingMoneyStatus fromValue(int value) {
        for (ChangingMoneyStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ChangingMoneyStatus value: " + value);
    }
}
