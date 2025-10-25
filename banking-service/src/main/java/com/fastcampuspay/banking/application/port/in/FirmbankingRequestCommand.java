package com.fastcampuspay.banking.application.port.in;

import com.fastcampuspay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class FirmbankingRequestCommand extends SelfValidating<FirmbankingRequestCommand> {
    @NotBlank
    private final String fromBankName;

    @NotBlank
    private final String fromBankAccountNumber;

    @NotBlank
    private final String toBankName;

    @NotBlank
    private final String toBankAccountNumber;

    private final Integer moneyAmount;

    @Builder
    public FirmbankingRequestCommand(String fromBankName, String fromBankAccountNumber, String toBankName, String toBankAccountNumber, Integer moneyAmount) {
        this.fromBankName = fromBankName;
        this.fromBankAccountNumber = fromBankAccountNumber;
        this.toBankName = toBankName;
        this.toBankAccountNumber = toBankAccountNumber;
        this.moneyAmount = moneyAmount;
        this.validateSelf();
    }
}
