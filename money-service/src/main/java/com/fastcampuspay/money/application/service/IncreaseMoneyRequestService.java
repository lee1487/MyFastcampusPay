package com.fastcampuspay.money.application.service;

import com.fastcampuspay.common.CnEnum.ChangingMoneyStatus;
import com.fastcampuspay.common.CnEnum.ChangingMoneyType;
import com.fastcampuspay.common.UseCase;
import com.fastcampuspay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.fastcampuspay.money.adapter.out.persistence.MoneyChangingRequestJpaEntity;
import com.fastcampuspay.money.adapter.out.persistence.MoneyChangingRequestMapper;
import com.fastcampuspay.money.application.port.in.IncreaseMoneyRequestCommand;
import com.fastcampuspay.money.application.port.in.IncreaseMoneyRequestUseCase;
import com.fastcampuspay.money.application.port.out.IncreaseMoneyPort;
import com.fastcampuspay.money.domain.MemberMoney;
import com.fastcampuspay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
@Transactional
public class IncreaseMoneyRequestService implements IncreaseMoneyRequestUseCase {
    private final MoneyChangingRequestMapper moneyChangingRequestMapper;
    private final IncreaseMoneyPort increaseMoneyPort;


    @Override
    public MoneyChangingRequest increaseMoneyRequest(IncreaseMoneyRequestCommand command) {

        // 머니의 충전.증액이라는 과정
        // 1. 고객 정보가 정상인지 확인(멤버)

        // 2. 고객의 연동된 계좌가 있는지, 고객의 연동된 계좌의 잔액이 충분한지도 확인(뱅킹)

        // 3. 법인 계좌 상태도 정상인지 확인(뱅킹)

        // 4. 증액을 위한 "기록". 요청상태로 MoneyChangeRequest를 생성한다. (MoneyChangingRequest)

        // 5. 펌뱅킹을 수행하고 (고객의 연동된 계좌 -> 패캠페이 법인 계좌) (뱅킹)

        // 6-1. 결과가 정상적이라면. 성공으로 MoneyChangingReuqest 상태값을 변동 후에 리턴
        MemberMoneyJpaEntity memberMoneyJpaEntity = increaseMoneyPort.increaseMoney(
                new MemberMoney.MembershipId(command.getTargetMembershipId()),
                command.getAmount()
        );
        // 성공 시 멤버의 MemberMoney 값 증액이 필요해요.
        if (memberMoneyJpaEntity != null) {
            MoneyChangingRequestJpaEntity moneyChangingRequest = increaseMoneyPort.createIncreasingMoneyChangingRequest(
                    new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
                    new MoneyChangingRequest.ChangingType(ChangingMoneyType.INCREASING),
                    new MoneyChangingRequest.ChangingMoneyAmount(command.getAmount()),
                    new MoneyChangingRequest.ChangingStatus(ChangingMoneyStatus.SUCCEEDED),
                    new MoneyChangingRequest.Uuid(UUID.randomUUID().toString())
            );

            return moneyChangingRequestMapper.mapToDomainEntity(moneyChangingRequest);
        }

        // 6-2 결과가 실패라면, 실패라고 MoneyChangingReuqest 상태값을 변동 후에 리턴
        return null;
    }
}
