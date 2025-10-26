package com.fastcampuspay.money.adapter.out.persistence;

import com.fastcampuspay.common.PersistenceAdapter;
import com.fastcampuspay.money.application.port.out.DecreaseMoneyPort;
import com.fastcampuspay.money.application.port.out.IncreaseMoneyPort;
import com.fastcampuspay.money.domain.MemberMoney;
import com.fastcampuspay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MoneyChangingRequestPersistanceAdapter implements IncreaseMoneyPort, DecreaseMoneyPort {
    private final SpringDataMoneyChangingRequestRepository springDataMoneyChangingRequestRepository;
    private final SpringDataMemberMoneyRepository springDataMemberMoneyRepository;

    @Override
    public MoneyChangingRequestJpaEntity createIncreasingMoneyChangingRequest(
            MoneyChangingRequest.TargetMembershipId targetMembershipId,
            MoneyChangingRequest.ChangingType moneyChangingType,
            MoneyChangingRequest.ChangingMoneyAmount changingMoneyAmount,
            MoneyChangingRequest.ChangingStatus moneyChangingStatus,
            MoneyChangingRequest.Uuid uuid
    ) {

        return springDataMoneyChangingRequestRepository.save(
                new MoneyChangingRequestJpaEntity(
                        targetMembershipId.getTargetMembershipId(),
                        moneyChangingType.getChangingTypeValue().getValue(),
                        changingMoneyAmount.getChangingMoneyAmountValue(),
                        new Date(System.currentTimeMillis()),
                        moneyChangingStatus.getChangingMoneyStatusValue().getValue(),
                        uuid.getUuidValue()
                )
        );
    }

    @Override
    public MemberMoneyJpaEntity increaseMoney(
            MemberMoney.MembershipId membershipId,
            int increaseMoneyAmount
    ) {

        List<MemberMoneyJpaEntity> entityList = springDataMemberMoneyRepository.findByMembershipId(membershipId.getMembershipId());
        var entity = entityList.get(0);

        if (entity == null) {
            // 회원 잔액 정보가 없으면 새로 생성
            entity = new MemberMoneyJpaEntity(
                    membershipId.getMembershipId(),
                    increaseMoneyAmount
            );
        } else {
            // 기존 잔액에 증액
            entity.setBalance(entity.getBalance() + increaseMoneyAmount);
        }

        return springDataMemberMoneyRepository.save(entity);
    }

    @Override
    public MoneyChangingRequestJpaEntity createDecreasingMoneyChangingRequest(
            MoneyChangingRequest.TargetMembershipId targetMembershipId,
            MoneyChangingRequest.ChangingType moneyChangingType,
            MoneyChangingRequest.ChangingMoneyAmount changingMoneyAmount,
            MoneyChangingRequest.ChangingStatus moneyChangingStatus,
            MoneyChangingRequest.Uuid uuid
    ) {

        return springDataMoneyChangingRequestRepository.save(
                new MoneyChangingRequestJpaEntity(
                        targetMembershipId.getTargetMembershipId(),
                        moneyChangingType.getChangingTypeValue().getValue(),
                        changingMoneyAmount.getChangingMoneyAmountValue(),
                        new Date(System.currentTimeMillis()),
                        moneyChangingStatus.getChangingMoneyStatusValue().getValue(),
                        uuid.getUuidValue()
                )
        );
    }

    @Override
    public MemberMoneyJpaEntity decreaseMoney(
            MemberMoney.MembershipId membershipId,
            int decreaseMoneyAmount
    ) {

        List<MemberMoneyJpaEntity> entityList = springDataMemberMoneyRepository.findByMembershipId(membershipId.getMembershipId());
        var entity = entityList.get(0);

        if (entity == null) {
            // 회원 잔액 정보가 없으면 음수 잔액으로 생성 (비즈니스 로직에 따라 예외 처리 필요할 수 있음)
            entity = new MemberMoneyJpaEntity(
                    membershipId.getMembershipId(),
                    -decreaseMoneyAmount
            );
        } else {
            // 기존 잔액에서 차감
            entity.setBalance(entity.getBalance() - decreaseMoneyAmount);
        }

        return springDataMemberMoneyRepository.save(entity);
    }
}
