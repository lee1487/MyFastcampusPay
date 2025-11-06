package com.fastcampuspay.money.domain;

import com.fastcampuspay.common.CnEnum.ChangingMoneyStatus;
import com.fastcampuspay.common.CnEnum.ChangingMoneyType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberMoney {
    @Getter
    private final String memberMoneyId;
    // 어떤 고객의 증액/감액 요청을 요청했는지의 멤버 정보
    @Getter
    private final String membershipId;
    // 그 요청이 증액 요청인지 / 감액 요청인지
    @Getter
    private final int balance;

    // @Getter private final int linkedBankAccount;

    public static MemberMoney generateMemberMoney(
            MemberMoneyId memberMoneyId,
            MembershipId membershipId,
            Balance balance
    ) {
        return new MemberMoney(
                memberMoneyId.memberMoneyId,
                membershipId.membershipId,
                balance.balance
        );
    }

    @Value
    public static class MemberMoneyId {
        public MemberMoneyId(String value) {
            this.memberMoneyId = value;
        }

        String memberMoneyId;
    }

    @Value
    public static class MembershipId {
        public MembershipId(String value) {
            this.membershipId = value;
        }

        String membershipId;
    }

    @Value
    public static class Balance {
        public Balance(int value) {
            this.balance = value;
        }

        int balance;
    }

    @Value
    public static class MoneyAggregateIdentifier {
        String aggregateIdentifier;
        public MoneyAggregateIdentifier(String aggregateIdentifier) {
            this.aggregateIdentifier = aggregateIdentifier;
        }
    }







}
