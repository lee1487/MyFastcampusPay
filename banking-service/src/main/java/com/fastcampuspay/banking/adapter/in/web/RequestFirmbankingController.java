package com.fastcampuspay.banking.adapter.in.web;

import com.fastcampuspay.banking.application.port.in.FirmbankingRequestUseCase;
import com.fastcampuspay.banking.application.port.in.FirmbankingRequestCommand;
import com.fastcampuspay.banking.domain.FirmbankingRequest;
import com.fastcampuspay.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class RequestFirmbankingController {
    private final FirmbankingRequestUseCase requestFirmbankingUseCase;

    @PostMapping(path = "/banking/firmbanking/register")
    FirmbankingRequest registeredBankAccount(@RequestBody RequestFirmbankingRequest request) {
        FirmbankingRequestCommand command = FirmbankingRequestCommand.builder()
                .fromBankName(request.getFromBankName())
                .fromBankAccountNumber(request.getFromBankAccountNumber())
                .toBankName(request.getToBankName())
                .toBankAccountNumber(request.getToBankAccountNumber())
                .moneyAmount(request.getMoneyAmount())
                .build();

        return requestFirmbankingUseCase.requestFirmbanking(command);
    }
}
