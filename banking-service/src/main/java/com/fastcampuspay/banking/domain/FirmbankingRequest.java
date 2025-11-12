package com.fastcampuspay.banking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FirmbankingRequest {
    @Getter
    private final String firmbankingRequestId;
    @Getter
    private final String fromBankName;
    @Getter
    private final String fromBankAccountNumber;
    @Getter
    private final String toBankName;
    @Getter
    private final String toBankAccountNumber;
    @Getter
    private final int moneyAmount;  // only won
    @Getter
    private final int firmbankingStatus; // 0: 요청, 1: 완료, 2: 실패
    @Getter
    private final String uuid;
    @Getter
    private final String aggregateIdentifier;

    public static FirmbankingRequest generateFirmbankingRequest(
            FirmbankingRequestId firmbankingRequestId,
            FromBankName fromBankName,
            FromBankAccountNumber fromBankAccountNumber,
            ToBankName toBankName,
            ToBankAccountNumber toBankAccountNumber,
            MoneyAmount moneyAmount,
            FirmbankingStatus firmbankingStatus,
            String uuid,
            FirmbankingAggregateIdentifier firmbankingAggregateIdentifier

    ) {
        return new FirmbankingRequest(
                firmbankingRequestId.firmbankingRequestId,
                fromBankName.fromBankNameValue,
                fromBankAccountNumber.fromBankAccountNumberValue,
                toBankName.toBankNameValue,
                toBankAccountNumber.toBankAccountNumberValue,
                moneyAmount.moneyAmountValue,
                firmbankingStatus.firmbankingStatusValue,
                uuid,
                firmbankingAggregateIdentifier.getAggregateIdentifier()
        );
    }

    @Value
    public static class FirmbankingRequestId {
        public FirmbankingRequestId(String value) {
            this.firmbankingRequestId = value;
        }

        String firmbankingRequestId;
    }

    @Value
    public static class FromBankName {
        public FromBankName(String value) {
            this.fromBankNameValue = value;
        }

        String fromBankNameValue;
    }

    @Value
    public static class FromBankAccountNumber {
        public FromBankAccountNumber(String value) {
            this.fromBankAccountNumberValue = value;
        }

        String fromBankAccountNumberValue;
    }

    @Value
    public static class ToBankName {
        public ToBankName(String value) {
            this.toBankNameValue = value;
        }

        String toBankNameValue;
    }

    @Value
    public static class ToBankAccountNumber {
        public ToBankAccountNumber(String value) {
            this.toBankAccountNumberValue = value;
        }

        String toBankAccountNumberValue;
    }

    @Value
    public static class MoneyAmount {
        public MoneyAmount(int value) {
            this.moneyAmountValue = value;
        }

        int moneyAmountValue;
    }

    @Value
    public static class FirmbankingStatus {
        public FirmbankingStatus(int value) {
            this.firmbankingStatusValue = value;
        }

        int firmbankingStatusValue;
    }

    @Value
    public static class FirmbankingAggregateIdentifier {
        public FirmbankingAggregateIdentifier(String value) {
            this.aggregateIdentifier = value;
        }
        String aggregateIdentifier;
    }
}
