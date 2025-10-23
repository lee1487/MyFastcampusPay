package com.fastcampuspay.banking.application.port.in;

import com.fastcampuspay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class RegisteredBankAccountCommand extends SelfValidating<RegisteredBankAccountCommand> {

    @NotNull
    private final String membershipId;
    @NotNull
    @NotBlank
    private final String bankName;
    @NotNull
    @NotBlank
    private final String bankAccountNumber;

    public RegisteredBankAccountCommand(String membershipId, String bankName, String bankAccountNumber) {
        this.membershipId = membershipId;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.validateSelf();
    }
}
