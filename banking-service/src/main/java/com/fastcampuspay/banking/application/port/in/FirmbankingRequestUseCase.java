package com.fastcampuspay.banking.application.port.in;

import com.fastcampuspay.banking.domain.FirmbankingRequest;

public interface FirmbankingRequestUseCase {
    FirmbankingRequest requestFirmbanking(FirmbankingRequestCommand command);
}
