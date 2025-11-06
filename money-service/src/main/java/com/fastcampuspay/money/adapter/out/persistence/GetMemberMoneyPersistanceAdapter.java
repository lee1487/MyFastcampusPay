package com.fastcampuspay.money.adapter.out.persistence;

import com.fastcampuspay.common.PersistenceAdapter;
import com.fastcampuspay.money.application.port.in.GetMemberMoneyPort;
import com.fastcampuspay.money.domain.MemberMoney;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class GetMemberMoneyPersistanceAdapter implements GetMemberMoneyPort {
    private final SpringDataMemberMoneyRepository memberMoneyRepository;

    @Override
    public MemberMoneyJpaEntity getMemberMoney(MemberMoney.MembershipId membershipId) {
        MemberMoneyJpaEntity entity;
        List<MemberMoneyJpaEntity> entityList = memberMoneyRepository.findByMembershipId(membershipId.getMembershipId());
        if (entityList.size() == 0) {
            entity = new MemberMoneyJpaEntity(
                    membershipId.getMembershipId(),
                    0,
                    UUID.randomUUID().toString()
            );
            return memberMoneyRepository.save(entity);
        }

        entity = entityList.get(0);
        if (entity.getAggregateIdentifier() == null) {
            entity.setAggregateIdentifier(UUID.randomUUID().toString());
            return memberMoneyRepository.save(entity);
        }

        return entityList.get(0);

    }
}
