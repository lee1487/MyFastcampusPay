package com.fastcampuspay.banking.application.service;

import com.fastcampuspay.banking.adapter.out.external.bank.ExternalFirmbankingRequest;
import com.fastcampuspay.banking.adapter.out.external.bank.FirmbankingResult;
import com.fastcampuspay.banking.adapter.out.persistence.FirmbankingRequestJpaEntity;
import com.fastcampuspay.banking.adapter.out.persistence.FirmbankingRequestMapper;
import com.fastcampuspay.banking.application.port.in.FirmbankingRequestCommand;
import com.fastcampuspay.banking.application.port.in.FirmbankingRequestUseCase;
import com.fastcampuspay.banking.application.port.out.FirmbankingRequestPort;
import com.fastcampuspay.banking.application.port.out.RequestExternalFirmbankingPort;
import com.fastcampuspay.banking.domain.FirmbankingRequest;
import com.fastcampuspay.common.UseCase;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
@Transactional
public class FirmbankingRequestService implements FirmbankingRequestUseCase {
    private final FirmbankingRequestMapper firmbankingRequestMapper;
    private final FirmbankingRequestPort firmbankingRequestPort;
    private final RequestExternalFirmbankingPort requestExternalFirmbankingPort;

    @Override
    public FirmbankingRequest requestFirmbanking(FirmbankingRequestCommand command) {

        // Business logic

        // a -> b 계좌

        // 1. 요청에 대한 정보를 먼저 write. "요청" 상태로
        FirmbankingRequestJpaEntity requestedJpaEntity = firmbankingRequestPort.createFirmbankingRequest(
                new FirmbankingRequest.FromBankName(command.getFromBankName()),
                new FirmbankingRequest.FromBankAccountNumber(command.getFromBankAccountNumber()),
                new FirmbankingRequest.ToBankName(command.getToBankName()),
                new FirmbankingRequest.ToBankAccountNumber(command.getToBankAccountNumber()),
                new FirmbankingRequest.MoneyAmount(command.getMoneyAmount()),
                new FirmbankingRequest.FirmbankingStatus(0),
                UUID.randomUUID()
        );

        // 2. 외부 은행에 펌뱅킹 요청
        FirmbankingResult firmbankingResult = requestExternalFirmbankingPort.requestExternalFirmbanking(new ExternalFirmbankingRequest(
                command.getFromBankName(),
                command.getFromBankAccountNumber(),
                command.getToBankName(),
                command.getToBankAccountNumber()
        ));

        // Transactional UUID
        String uuid = UUID.randomUUID().toString();
        requestedJpaEntity.setUuid(uuid);
        // 3. 결과에 따라서 1번에서 작성했던 FirmbankingRequest 정보를 Update
        if (firmbankingResult.getResultCode() == 0) {
            // 성공
            requestedJpaEntity.setFirmbankingStatusValue(1);
        } else {
            // 실패
            requestedJpaEntity.setFirmbankingStatusValue(2);
        }

        // 4. 결과를 리턴
        FirmbankingRequestJpaEntity jpaEntity = firmbankingRequestPort.modifyFirmbankingRequest(requestedJpaEntity);
        return firmbankingRequestMapper.mapToDomainEntity(jpaEntity, uuid);
    }
}
