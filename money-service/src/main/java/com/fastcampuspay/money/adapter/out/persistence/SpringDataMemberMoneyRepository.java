package com.fastcampuspay.money.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataMemberMoneyRepository extends JpaRepository<MemberMoneyJpaEntity, Long> {

    @Query("SELECT m FROM MemberMoneyJpaEntity m WHERE m.membershipId = :membershipId")
    List<MemberMoneyJpaEntity> findByMembershipId(String membershipId);
}
