package com.fastcampuspay.banking.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataRegisteredBankAccountRepository extends JpaRepository<RegisteredBankAccountJpaEntity, Long> {
    boolean existsByBankAccountNumber(String bankAccountNumber);
    Optional<RegisteredBankAccountJpaEntity> findByMembershipIdAndBankAccountNumber(String membershipId, String bankAccountNumber);
}
