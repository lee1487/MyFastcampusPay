package com.fastcampuspay.remittance.adapter.out.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRemittanceRequestRepository extends JpaRepository<RemittanceRequestJpaEntity, Long> {
}
