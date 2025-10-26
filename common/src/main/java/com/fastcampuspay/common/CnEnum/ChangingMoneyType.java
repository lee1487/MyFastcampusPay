package com.fastcampuspay.common.CnEnum;

public enum ChangingMoneyType {
    INCREASING(0),   // 증액
    DECREASING(1);   // 감액

    private final int value;

    ChangingMoneyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ChangingMoneyType fromValue(int value) {
        for (ChangingMoneyType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid ChangingMoneyType value: " + value);
    }
}
