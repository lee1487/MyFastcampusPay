package com.fastcampuspay.banking.application.port.in;

import com.fastcampuspay.banking.domain.FirmbankingRequest;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;

public interface FindFirmbankingRequestUseCase {
    FirmbankingRequest findFirmbankingRequest(FindFirmbankingRequestCommand command);
}
