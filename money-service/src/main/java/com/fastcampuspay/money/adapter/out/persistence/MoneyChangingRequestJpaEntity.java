package com.fastcampuspay.money.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "money_changing_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyChangingRequestJpaEntity {

    @Id
    @GeneratedValue
    private Long moneyChangingRequestId;
    private String targetMembershipId;
    private int moneyChangingType;
    private int moneyAmount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    private int changinsMoneyStatus; // 0: 요청, 1: 성공, 2: 실패
    private String uuid;

    public MoneyChangingRequestJpaEntity(String targetMembershipId, int moneyChangingType, int moneyAmount, Date timestamp, int changinsMoneyStatus, String uuid) {
        this.targetMembershipId = targetMembershipId;
        this.moneyChangingType = moneyChangingType;
        this.moneyAmount = moneyAmount;
        this.timestamp = timestamp;
        this.changinsMoneyStatus = changinsMoneyStatus;
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "MoneyChangingRequestJpaEntity{" +
                "moneyChangingRequestId=" + moneyChangingRequestId +
                ", targetMembershipId='" + targetMembershipId + '\'' +
                ", moneyChangingType=" + moneyChangingType +
                ", moneyAmount=" + moneyAmount +
                ", timestamp=" + timestamp +
                ", changinsMoneyStatus=" + changinsMoneyStatus +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
