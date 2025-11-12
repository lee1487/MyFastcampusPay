package com.fastcampuspay.banking.adapter.axon.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmbankingRequestCreatedEvent {
    private String fromBankName;
    private String fromBankAccountNumber;

    private String toBankName;
    private String toBankAccountNumber;

    private int moneyAmount;
}
