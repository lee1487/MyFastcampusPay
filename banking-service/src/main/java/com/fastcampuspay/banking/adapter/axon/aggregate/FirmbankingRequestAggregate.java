package com.fastcampuspay.banking.adapter.axon.aggregate;

import com.fastcampuspay.banking.adapter.axon.command.CreateFirmbankingRequestCommand;
import com.fastcampuspay.banking.adapter.axon.event.FirmbankingRequestCreatedEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate()
@Data
public class FirmbankingRequestAggregate {
    @AggregateIdentifier
    private String id;

    private String fromBankName;
    private String fromBankAccountNumber;
    private String toBankName;
    private String toBankAccountNumber;
    private int moneyAmount;
    private int firmbankingStatus;

    @CommandHandler
    public FirmbankingRequestAggregate (CreateFirmbankingRequestCommand command) {
        System.out.println("CreateFirmbankingRequestCommand Handler");
        apply(new FirmbankingRequestCreatedEvent(command.getFromBankName(), command.getFromBankAccountNumber(), command.getToBankName(), command.getToBankAccountNumber(), command.getMoneyAmount()));

    }

    @EventSourcingHandler
    public void on(FirmbankingRequestCreatedEvent event) {
        System.out.println("FirmbankingRequestCreatedEvent Sourcing Handler");
        id = UUID.randomUUID().toString();
        fromBankName = event.getFromBankName();
        fromBankAccountNumber = event.getFromBankAccountNumber();
        toBankName = event.getToBankName();
        toBankAccountNumber = event.getToBankAccountNumber();

    }


    public FirmbankingRequestAggregate() {
    }
}
