package com.fastcampuspay.banking.application.port.in;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class FindBankAccountCommand {
    private String membershipId;
    private String bankAccountNumber;

    public FindBankAccountCommand(String membershipId, String bankAccountNumber) {
        this.membershipId = membershipId;
        this.bankAccountNumber = bankAccountNumber;
    }
}
