package com.fastcampuspay.banking.adapter.out.persistence;

import com.fastcampuspay.banking.domain.FirmbankingRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FirmbankingRequestMapper {
    public FirmbankingRequest mapToDomainEntity(FirmbankingRequestJpaEntity jpaEntity, String uuid) {
        return FirmbankingRequest.generateFirmbankingRequest(
                new FirmbankingRequest.FirmbankingRequestId(jpaEntity.getRequestFirmbankingId()+""),
                new FirmbankingRequest.FromBankName(jpaEntity.getFromBankName()),
                new FirmbankingRequest.FromBankAccountNumber(jpaEntity.getFromBankAccountNumber()),
                new FirmbankingRequest.ToBankName(jpaEntity.getToBankName()),
                new FirmbankingRequest.ToBankAccountNumber(jpaEntity.getToBankAccountNumber()),
                new FirmbankingRequest.MoneyAmount(jpaEntity.getMoneyAmount()),
                new FirmbankingRequest.FirmbankingStatus(jpaEntity.getFirmbankingStatusValue()),
                uuid,
                new FirmbankingRequest.FirmbankingAggregateIdentifier(jpaEntity.getAggregateIdentifier())
        );
    }
}
