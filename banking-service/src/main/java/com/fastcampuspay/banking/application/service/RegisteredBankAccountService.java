package com.fastcampuspay.banking.application.service;

import com.fastcampuspay.banking.adapter.out.external.bank.BankAccount;
import com.fastcampuspay.banking.adapter.out.external.bank.GetBankAccountRequest;
import com.fastcampuspay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.fastcampuspay.banking.adapter.out.persistence.RegisteredBankAccountMapper;
import com.fastcampuspay.banking.application.port.in.RegisteredBankAccountCommand;
import com.fastcampuspay.banking.application.port.in.RegisteredBankAccountUseCase;
import com.fastcampuspay.banking.application.port.out.FindBankAccountPort;
import com.fastcampuspay.banking.application.port.out.RegisteredBankAccountPort;
import com.fastcampuspay.banking.application.port.out.RequestBankAccountInfoPort;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.UseCase;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class RegisteredBankAccountService implements RegisteredBankAccountUseCase {
    private final RegisteredBankAccountPort registeredBankAccountPort;
    private final RegisteredBankAccountMapper registeredBankAccountMapper;
    private final FindBankAccountPort findBankAccountPort;

    private final RequestBankAccountInfoPort requestBankAccountInfoPort;

    @Override
    public RegisteredBankAccount registerBankAccount(RegisteredBankAccountCommand command) {

        // 은행 계좌를 등록해야하는 서비스 (비즈니스 로직)
        // command.getMembershipId()

        // (멤버 서비스도 확인?) 여기서는 skip

        // 1. 외부 살제 은행에 등록이 가능한 계좌인지(정상인지) 확인한다.
        // 외부 은행에 이 계좌가 정상인지? 확인해야 한다.
        // biz logic -> External System

        // 실제 외부의 은행 계좌 정보를 Get
        GetBankAccountRequest req = new GetBankAccountRequest(command.getBankName(), command.getBankAccountNumber());
        BankAccount bankAccount = requestBankAccountInfoPort.getBankAccountInfo(req);
        boolean accountIsValid = bankAccount.isLinkedStatusIsValid();

        // port -> adapter -> external system
        // 2. 등록가능한 계좌라면, 등록한다. 성공하면, 등록에 성공한 등록 정보를 리턴
        // 2-1. 등록가능하지 않은 계좌라면, 에러를 리턴
        if (accountIsValid) {
            // bankAccountNumber 중복 확인
            boolean isDuplicate = findBankAccountPort.existsByBankAccountNumber(
                    new RegisteredBankAccount.BankAccountNumber(command.getBankAccountNumber())
            );
            
            if (isDuplicate) {
                // 중복된 계좌번호인 경우 null 반환
                return null;
            }
            
            RegisteredBankAccountJpaEntity jpaEntity = registeredBankAccountPort.createRegisteredBankAccount(
                    new RegisteredBankAccount.MembershipId(command.getMembershipId()),
                    new RegisteredBankAccount.BankName(command.getBankName()),
                    new RegisteredBankAccount.BankAccountNumber(command.getBankAccountNumber()),
                    new RegisteredBankAccount.LinkedStatusIsValid(accountIsValid)
            );
            return registeredBankAccountMapper.mapToDomainEntity(jpaEntity);
        } else {
            return null;
        }
    }
}
