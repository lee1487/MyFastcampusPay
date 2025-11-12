package com.fastcampuspay.banking.application.port.out;

import com.fastcampuspay.banking.adapter.out.persistence.FirmbankingRequestJpaEntity;
import com.fastcampuspay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.fastcampuspay.banking.domain.FirmbankingRequest;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;

import java.util.UUID;

public interface FirmbankingRequestPort {
    FirmbankingRequestJpaEntity createFirmbankingRequest(
            FirmbankingRequest.FromBankName fromBankName,
            FirmbankingRequest.FromBankAccountNumber fromBankAccountNumber,
            FirmbankingRequest.ToBankName toBankName,
            FirmbankingRequest.ToBankAccountNumber toBankAccountNumber,
            FirmbankingRequest.MoneyAmount moneyAmount,
            FirmbankingRequest.FirmbankingStatus firmbankingStatus,
            UUID uuid,
            FirmbankingRequest.FirmbankingAggregateIdentifier aggregateIdentifier
    );

    FirmbankingRequestJpaEntity modifyFirmbankingRequest(FirmbankingRequestJpaEntity jpaEntity);
    FirmbankingRequestJpaEntity findFirmbankingRequest(FirmbankingRequest.FirmbankingRequestId firmbankingRequestId);
}
