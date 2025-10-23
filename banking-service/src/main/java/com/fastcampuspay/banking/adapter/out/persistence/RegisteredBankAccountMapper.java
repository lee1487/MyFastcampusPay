package com.fastcampuspay.banking.adapter.out.persistence;

import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import org.springframework.stereotype.Component;

@Component
public class RegisteredBankAccountMapper {
    public RegisteredBankAccount mapToDomainEntity(RegisteredBankAccountJpaEntity jpaEntity) {
        return RegisteredBankAccount.generateRegisteredBankAccount(
                new RegisteredBankAccount.RegisteredBankAccountId(jpaEntity.getMembershipId()),
                new RegisteredBankAccount.MembershipId(jpaEntity.getMembershipId()),
                new RegisteredBankAccount.BankName(jpaEntity.getBankName()),
                new RegisteredBankAccount.BankAccountNumber(jpaEntity.getBankAccountNumber()),
                new RegisteredBankAccount.LinkedStatusIsValid(jpaEntity.isLinkedStatusIsValid())
        );
    }
}
