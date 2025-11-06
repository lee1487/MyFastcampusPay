package com.fastcampuspay.money.adapter.axon.event;

import com.fastcampuspay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class IncreaseMemberMoneyEvent extends SelfValidating<IncreaseMemberMoneyEvent> {
    private String aggregateIdentifier;
    private String membershipId;
    private int amount;

    public IncreaseMemberMoneyEvent(String aggregateIdentifier, String membershipId, int amount) {
        this.aggregateIdentifier = aggregateIdentifier;
        this.membershipId = membershipId;
        this.amount = amount;
    }

    public IncreaseMemberMoneyEvent() {}
}
