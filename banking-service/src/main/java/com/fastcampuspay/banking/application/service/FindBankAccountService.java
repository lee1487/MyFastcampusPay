package com.fastcampuspay.banking.application.service;

import com.fastcampuspay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.fastcampuspay.banking.adapter.out.persistence.RegisteredBankAccountMapper;
import com.fastcampuspay.banking.application.port.in.FindBankAccountCommand;
import com.fastcampuspay.banking.application.port.in.FindBankAccountUseCase;
import com.fastcampuspay.banking.application.port.out.FindBankAccountPort;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.UseCase;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.Optional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class FindBankAccountService implements FindBankAccountUseCase {
    private final FindBankAccountPort findBankAccountPort;
    private final RegisteredBankAccountMapper registeredBankAccountMapper;

    @Override
    public RegisteredBankAccount findBankAccount(FindBankAccountCommand command) {
        // 멤버십 ID와 계좌번호로 등록된 계좌를 찾습니다
        Optional<RegisteredBankAccountJpaEntity> jpaEntity = findBankAccountPort.findByMembershipIdAndBankAccountNumber(
                new RegisteredBankAccount.MembershipId(command.getMembershipId()),
                new RegisteredBankAccount.BankAccountNumber(command.getBankAccountNumber())
        );

        return jpaEntity.map(registeredBankAccountMapper::mapToDomainEntity).orElse(null);
    }
}
