package com.fastcampuspay.banking.adapter.in.web;

import com.fastcampuspay.banking.application.port.in.FindBankAccountCommand;
import com.fastcampuspay.banking.application.port.in.FindBankAccountUseCase;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fastcampuspay.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class FindBankAccountController {
    private final FindBankAccountUseCase findBankAccountUseCase;

    @PostMapping(path = "/banking/account/find")
    RegisteredBankAccount findBankAccount(@RequestBody FindBankAccountRequest request) {
        FindBankAccountCommand command = FindBankAccountCommand.builder()
                .membershipId(request.getMembershipId())
                .bankAccountNumber(request.getBankAccountNumber())
                .build();

        return findBankAccountUseCase.findBankAccount(command);
    }
}
