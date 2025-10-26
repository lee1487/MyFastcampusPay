package com.fastcampuspay.money.application.port.in;

import com.fastcampuspay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class DecreaseMoneyRequestCommand extends SelfValidating<DecreaseMoneyRequestCommand> {

    @NotBlank
    private String targetMembershipId;
    // 무조건 증액 요청 (충전)
    @Positive
    private int amount;

    @Builder
    public DecreaseMoneyRequestCommand(String targetMembershipId, int amount) {
        this.targetMembershipId = targetMembershipId;
        this.amount = amount;
        this.validateSelf();
    }
}
