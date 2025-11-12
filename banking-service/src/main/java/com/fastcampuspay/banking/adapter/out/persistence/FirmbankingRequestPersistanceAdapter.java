package com.fastcampuspay.banking.adapter.out.persistence;

import com.fastcampuspay.banking.application.port.out.FirmbankingRequestPort;
import com.fastcampuspay.banking.application.port.out.RegisteredBankAccountPort;
import com.fastcampuspay.banking.domain.FirmbankingRequest;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class FirmbankingRequestPersistanceAdapter implements FirmbankingRequestPort {
    private final SpringDataFirmbankingRequestRepository firmbankingRequestRepository;


    @Override
    public FirmbankingRequestJpaEntity createFirmbankingRequest(
            FirmbankingRequest.FromBankName fromBankName,
            FirmbankingRequest.FromBankAccountNumber fromBankAccountNumber,
            FirmbankingRequest.ToBankName toBankName,
            FirmbankingRequest.ToBankAccountNumber toBankAccountNumber,
            FirmbankingRequest.MoneyAmount moneyAmount,
            FirmbankingRequest.FirmbankingStatus firmbankingStatus,
            UUID uuid,
            FirmbankingRequest.FirmbankingAggregateIdentifier aggregateIdentifier
    ) {

        return firmbankingRequestRepository.save(
                new FirmbankingRequestJpaEntity(
                        fromBankName.getFromBankNameValue(),
                        fromBankAccountNumber.getFromBankAccountNumberValue(),
                        toBankName.getToBankNameValue(),
                        toBankAccountNumber.getToBankAccountNumberValue(),
                        moneyAmount.getMoneyAmountValue(),
                        firmbankingStatus.getFirmbankingStatusValue(),
                        uuid.toString(),
                        aggregateIdentifier.getAggregateIdentifier()
                )
        );
    }

    @Override
    public FirmbankingRequestJpaEntity modifyFirmbankingRequest(FirmbankingRequestJpaEntity jpaEntity) {
        return firmbankingRequestRepository.save(jpaEntity);
    }

    @Override
    public FirmbankingRequestJpaEntity findFirmbankingRequest(FirmbankingRequest.FirmbankingRequestId firmbankingRequestId) {
        return firmbankingRequestRepository.getById(Long.parseLong(firmbankingRequestId.getFirmbankingRequestId()));
    }
}
