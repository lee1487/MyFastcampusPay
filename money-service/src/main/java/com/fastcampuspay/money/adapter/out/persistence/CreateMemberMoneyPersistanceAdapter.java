package com.fastcampuspay.money.adapter.out.persistence;

import com.fastcampuspay.common.PersistenceAdapter;
import com.fastcampuspay.money.application.port.out.CreateMemberMoneyPort;
import com.fastcampuspay.money.domain.MemberMoney;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateMemberMoneyPersistanceAdapter implements CreateMemberMoneyPort {
    private final SpringDataMemberMoneyRepository memberMoneyRepository;

    @Override
    public MemberMoneyJpaEntity createMemberMoney(MemberMoney.MembershipId membershipId, MemberMoney.MoneyAggregateIdentifier moneyAggregateIdentifier) {
        MemberMoneyJpaEntity entity;
        List<MemberMoneyJpaEntity> entityList = memberMoneyRepository.findByMembershipId(membershipId.getMembershipId());
        if (entityList.isEmpty()) {
            entity = new MemberMoneyJpaEntity(
                    membershipId.getMembershipId(),
                    0,
                    moneyAggregateIdentifier.getAggregateIdentifier()
            );

            entity = memberMoneyRepository.save(entity);
            return entity;
        }

        return entityList.get(0);
    }
}
