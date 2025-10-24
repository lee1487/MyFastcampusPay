package com.fastcampuspay.banking.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindBankAccountRequest {
    private String membershipId;
    private String bankAccountNumber;
}
