package com.fastcampuspay.money.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "member_money")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberMoneyJpaEntity {

    @Id
    @GeneratedValue
    private Long memberMoneyId;
    private String membershipId;
    private int balance;

    public MemberMoneyJpaEntity(String membershipId, int balance) {
        this.membershipId = membershipId;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "MemberMoneyJpaEntity{" +
                "memberMoneyId=" + memberMoneyId +
                ", membershipId='" + membershipId + '\'' +
                ", balance=" + balance +
                '}';
    }
}
