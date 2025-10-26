package com.fastcampuspay.money.adapter.out.persistence;

import com.fastcampuspay.money.domain.MoneyChangingRequest;
import com.fastcampuspay.common.CnEnum.ChangingMoneyStatus;
import com.fastcampuspay.common.CnEnum.ChangingMoneyType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MoneyChangingRequestMapper {
    public MoneyChangingRequest mapToDomainEntity(MoneyChangingRequestJpaEntity jpaEntity) {
        return MoneyChangingRequest.generateMoneyChangingRequest(
                new MoneyChangingRequest.MoneyChangingRequestId(jpaEntity.getMoneyChangingRequestId() + ""),
                new MoneyChangingRequest.TargetMembershipId(jpaEntity.getTargetMembershipId()),
                new MoneyChangingRequest.ChangingType(ChangingMoneyType.fromValue(jpaEntity.getMoneyChangingType())),
                new MoneyChangingRequest.ChangingMoneyAmount(jpaEntity.getMoneyAmount()),
                new MoneyChangingRequest.ChangingStatus(ChangingMoneyStatus.fromValue(jpaEntity.getChanginsMoneyStatus())),
                new MoneyChangingRequest.Uuid(jpaEntity.getUuid())
        );
    }
}
