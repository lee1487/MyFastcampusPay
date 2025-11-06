package com.fastcampuspay.money.application.service;

import com.fastcampuspay.common.UseCase;
import com.fastcampuspay.money.adapter.axon.command.MemberMoneyCreatedCommand;
import com.fastcampuspay.money.application.port.in.CreateMemberMoneyCommand;
import com.fastcampuspay.money.application.port.in.CreateMemberMoneyUseCase;
import com.fastcampuspay.money.application.port.out.CreateMemberMoneyPort;
import com.fastcampuspay.money.domain.MemberMoney;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.transaction.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class CreateMemberMoneyService implements CreateMemberMoneyUseCase {
    private final CommandGateway commandGateway;
    private final CreateMemberMoneyPort createMemberMoneyPort;


    @Override
    public void createMemberMoney(CreateMemberMoneyCommand command) {
        MemberMoneyCreatedCommand axonCommand = new MemberMoneyCreatedCommand(command.getMembershipId());

        commandGateway.send(axonCommand).whenComplete((result, throwable) -> {
            if (throwable != null) {
                System.out.println("throwable = " + throwable);
                throw new RuntimeException(throwable);
            } else {
                System.out.println("result = " + result);
                createMemberMoneyPort.createMemberMoney(
                        new MemberMoney.MembershipId(command.getMembershipId()),
                        new MemberMoney.MoneyAggregateIdentifier(result.toString())
                );
            }
        });


    }
}
