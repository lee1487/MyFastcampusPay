package com.fastcampuspay.banking.adapter.out.external.bank;

import com.fastcampuspay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.fastcampuspay.banking.adapter.out.persistence.SpringDataRegisteredBankAccountRepository;
import com.fastcampuspay.banking.application.port.out.RegisteredBankAccountPort;
import com.fastcampuspay.banking.application.port.out.RequestBankAccountInfoPort;
import com.fastcampuspay.banking.application.port.out.RequestExternalFirmbankingPort;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.ExternalSystemAdapter;
import com.fastcampuspay.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@ExternalSystemAdapter
@RequiredArgsConstructor
public class BankAccountAdapter implements RequestBankAccountInfoPort, RequestExternalFirmbankingPort {

    @Override
    public BankAccount getBankAccountInfo(GetBankAccountRequest request) {
        // 실제로 외부 은행에 http를 통해서
        // 실제 은행 계좌 정보를 가져오고

        // 실제 은행 계좌 -> BankAccount

        return new BankAccount(request.getBankName(), request.getBankAccountNumber(), true);
    }

    @Override
    public FirmbankingResult requestExternalFirmbanking(ExternalFirmbankingRequest request) {
        // 실제로 외부 은행에 http를 통해서
        // 펌뱅킹 요청을 하고

        // 그 결과를
        // 외부 은행의 실제 결과를 -> 패캠 페이의 FirmbankingResult 파싱
        var result = new FirmbankingResult(0);
        return result;
    }
}
