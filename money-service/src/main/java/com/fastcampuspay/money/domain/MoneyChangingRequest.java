package com.fastcampuspay.money.domain;

import com.fastcampuspay.common.CnEnum.ChangingMoneyStatus;
import com.fastcampuspay.common.CnEnum.ChangingMoneyType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoneyChangingRequest {
    @Getter
    private final String moneyChangingRequestId;
    // 어떤 고객의 증액/감액 요청을 요청했는지의 멤버 정보
    @Getter
    private final String targetMembershipId;
    // 그 요청이 증액 요청인지 / 감액 요청인지
    @Getter
    private final ChangingMoneyType changingMoneyType; // enum 0: 증액, 1: 감액

    // 증액 또는 감액 요청의 금액
    @Getter
    private final int changingMoneyAmount;

    // 머니 변액 요청에 대한 상태
    @Getter
    private final ChangingMoneyStatus changingMoneyStatus; // enum

    @Getter
    private final String uuid;

    @Getter
    private final Date createdAt;

    public static MoneyChangingRequest generateMoneyChangingRequest(
            MoneyChangingRequestId moneyChangingRequestId,
            TargetMembershipId targetMembershipId,
            ChangingType changingType,
            ChangingMoneyAmount changingMoneyAmount,
            ChangingStatus changingMoneyStatus,
            Uuid uuid
    ) {
        return new MoneyChangingRequest(
                moneyChangingRequestId.moneyChangingRequestId,
                targetMembershipId.targetMembershipId,
                changingType.changingTypeValue,
                changingMoneyAmount.changingMoneyAmountValue,
                changingMoneyStatus.changingMoneyStatusValue,
                uuid.getUuidValue(),
                new Date()
        );
    }

    @Value
    public static class MoneyChangingRequestId {
        public MoneyChangingRequestId(String value) {
            this.moneyChangingRequestId = value;
        }

        String moneyChangingRequestId;
    }

    @Value
    public static class TargetMembershipId {
        public TargetMembershipId(String value) {
            this.targetMembershipId = value;
        }

        String targetMembershipId;
    }

    @Value
    public static class ChangingType {
        public ChangingType(ChangingMoneyType value) {
            this.changingTypeValue = value;
        }

        ChangingMoneyType changingTypeValue;
    }

    @Value
    public static class ChangingMoneyAmount {
        public ChangingMoneyAmount(int value) {
            this.changingMoneyAmountValue = value;
        }

        int changingMoneyAmountValue;
    }

    @Value
    public static class ChangingStatus {
        public ChangingStatus(ChangingMoneyStatus value) {
            this.changingMoneyStatusValue = value;
        }

        ChangingMoneyStatus changingMoneyStatusValue;
    }

    @Value
    public static class Uuid {
        public Uuid(String value) {
            this.uuidValue = value;
        }

        String uuidValue;
    }

    @Value
    public static class CreatedAt {
        public CreatedAt(Date value) {
            this.createdAtValue = value;
        }

        Date createdAtValue;
    }





}
