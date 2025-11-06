package com.fastcampuspay.money.application.port.out;

import com.fastcampuspay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.fastcampuspay.money.domain.MemberMoney;

public interface CreateMemberMoneyPort {
    MemberMoneyJpaEntity createMemberMoney(
            MemberMoney.MembershipId membershipId,
            MemberMoney.MoneyAggregateIdentifier moneyAggregateIdentifier
    );
}
