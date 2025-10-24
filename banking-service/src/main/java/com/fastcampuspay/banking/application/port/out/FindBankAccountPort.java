package com.fastcampuspay.banking.application.port.out;

import com.fastcampuspay.banking.adapter.out.persistence.RegisteredBankAccountJpaEntity;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;

import java.util.Optional;

public interface FindBankAccountPort {
    boolean existsByBankAccountNumber(RegisteredBankAccount.BankAccountNumber bankAccountNumber);
    Optional<RegisteredBankAccountJpaEntity> findByMembershipIdAndBankAccountNumber(
            RegisteredBankAccount.MembershipId membershipId, 
            RegisteredBankAccount.BankAccountNumber bankAccountNumber);
}
