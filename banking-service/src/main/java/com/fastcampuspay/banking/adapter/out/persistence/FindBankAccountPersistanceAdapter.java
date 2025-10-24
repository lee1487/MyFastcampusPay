package com.fastcampuspay.banking.adapter.out.persistence;

import com.fastcampuspay.banking.application.port.out.FindBankAccountPort;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindBankAccountPersistanceAdapter implements FindBankAccountPort {
    private final SpringDataRegisteredBankAccountRepository registeredBankAccountRepository;

    @Override
    public boolean existsByBankAccountNumber(RegisteredBankAccount.BankAccountNumber bankAccountNumber) {
        return registeredBankAccountRepository.existsByBankAccountNumber(bankAccountNumber.getBankAccountNumberValue());
    }

    @Override
    public Optional<RegisteredBankAccountJpaEntity> findByMembershipIdAndBankAccountNumber(
            RegisteredBankAccount.MembershipId membershipId, 
            RegisteredBankAccount.BankAccountNumber bankAccountNumber) {
        return registeredBankAccountRepository.findByMembershipIdAndBankAccountNumber(
                membershipId.getMembershipId(),
                bankAccountNumber.getBankAccountNumberValue());
    }
}
