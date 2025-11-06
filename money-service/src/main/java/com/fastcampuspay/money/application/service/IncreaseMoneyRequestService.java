package com.fastcampuspay.money.application.service;

import com.fastcampuspay.common.CnEnum.ChangingMoneyStatus;
import com.fastcampuspay.common.CnEnum.ChangingMoneyType;
import com.fastcampuspay.common.CnEnum.SubTaskStatus;
import com.fastcampuspay.common.UseCase;
import com.fastcampuspay.common.task.CountDownLatchManager;
import com.fastcampuspay.common.task.RechargingMoneyTask;
import com.fastcampuspay.common.task.SubTask;
import com.fastcampuspay.money.adapter.axon.command.IncreaseMemberMoneyCommand;
import com.fastcampuspay.money.adapter.out.persistence.MemberMoneyJpaEntity;
import com.fastcampuspay.money.adapter.out.persistence.MoneyChangingRequestJpaEntity;
import com.fastcampuspay.money.adapter.out.persistence.MoneyChangingRequestMapper;
import com.fastcampuspay.money.application.port.in.GetMemberMoneyPort;
import com.fastcampuspay.money.application.port.in.IncreaseMoneyRequestCommand;
import com.fastcampuspay.money.application.port.in.IncreaseMoneyRequestUseCase;
import com.fastcampuspay.money.application.port.out.IncreaseMoneyPort;
import com.fastcampuspay.money.application.port.out.SendRechargingMoneyTaskPort;
import com.fastcampuspay.money.application.port.out.membership.GetMembershipPort;
import com.fastcampuspay.money.application.port.out.membership.MembershipStatus;
import com.fastcampuspay.money.domain.MemberMoney;
import com.fastcampuspay.money.domain.MoneyChangingRequest;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
@Transactional
public class IncreaseMoneyRequestService implements IncreaseMoneyRequestUseCase {
    private final CountDownLatchManager countDownLatchManager;
    private final SendRechargingMoneyTaskPort sendRechargingMoneyTaskPort;
    private final GetMembershipPort membershipPort;
    private final MoneyChangingRequestMapper moneyChangingRequestMapper;
    private final IncreaseMoneyPort increaseMoneyPort;
    private final GetMemberMoneyPort getMemberMoneyPort;
    private final CommandGateway commandGateway;


    @Override
    public MoneyChangingRequest increaseMoneyRequest(IncreaseMoneyRequestCommand command) {

        // 머니의 충전.증액이라는 과정
        // 1. 고객 정보가 정상인지 확인(멤버)
        MembershipStatus membership = membershipPort.getMembership(command.getTargetMembershipId());


        // 2. 고객의 연동된 계좌가 있는지, 고객의 연동된 계좌의 잔액이 충분한지도 확인(뱅킹)

        // 3. 법인 계좌 상태도 정상인지 확인(뱅킹)

        // 4. 증액을 위한 "기록". 요청상태로 MoneyChangeRequest를 생성한다. (MoneyChangingRequest)

        // 5. 펌뱅킹을 수행하고 (고객의 연동된 계좌 -> 패캠페이 법인 계좌) (뱅킹)

        // 6-1. 결과가 정상적이라면. 성공으로 MoneyChangingReuqest 상태값을 변동 후에 리턴
        MemberMoneyJpaEntity memberMoneyJpaEntity = increaseMoneyPort.increaseMoney(
                new MemberMoney.MembershipId(command.getTargetMembershipId()),
                command.getAmount()
        );
        // 성공 시 멤버의 MemberMoney 값 증액이 필요해요.
        if (memberMoneyJpaEntity != null) {
            MoneyChangingRequestJpaEntity moneyChangingRequest = increaseMoneyPort.createIncreasingMoneyChangingRequest(
                    new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
                    new MoneyChangingRequest.ChangingType(ChangingMoneyType.INCREASING),
                    new MoneyChangingRequest.ChangingMoneyAmount(command.getAmount()),
                    new MoneyChangingRequest.ChangingStatus(ChangingMoneyStatus.SUCCEEDED),
                    new MoneyChangingRequest.Uuid(UUID.randomUUID().toString())
            );

            return moneyChangingRequestMapper.mapToDomainEntity(moneyChangingRequest);
        }

        // 6-2 결과가 실패라면, 실패라고 MoneyChangingReuqest 상태값을 변동 후에 리턴
        return null;
    }

    @Override
    public MoneyChangingRequest increaseMoneyRequestAsync(IncreaseMoneyRequestCommand command) {
        // SubTask
        // 각 서비스에 특정 membershipId로 Validation을 하기 위한 Task

        // 1. SubTask, Task
        SubTask validMemberSubTask = SubTask.builder()
                .subTaskName("validMemberTask : " + "멤버십 유효성 검사")
                .membershipId(command.getTargetMembershipId())
                .taskType("membership")
                .status(SubTaskStatus.READY.getValue())
                .build();


        // Banking Sub Task
        // Banking Account Validation
        SubTask validBankAccountTask = SubTask.builder()
                .subTaskName("validBankingAccountTask : " + "뱅킹 계좌 유효성 검사")
                .membershipId(command.getTargetMembershipId())
                .taskType("banking")
                .status(SubTaskStatus.READY.getValue())
                .build();

        // Amount Money Firmbanking --> 무조건 ok 받았다고 가정. TODO.

        // 2. Kafka Cluster Produce
        List<SubTask> subTaskList = new ArrayList<>();
        subTaskList.add(validMemberSubTask);
        subTaskList.add(validBankAccountTask);

        RechargingMoneyTask mainTask = RechargingMoneyTask.builder()
                .taskId(UUID.randomUUID().toString())
                .taskName("Increase Money Task / 머니 충전 Task")
                .subTaskList(subTaskList)
                .moneyAmount(command.getAmount())
                .membershipId(command.getTargetMembershipId())
                .toBankName("fastcampus")
                .build();
        // Task Produce
        sendRechargingMoneyTaskPort.sendRechargingMoneyTaskPort(mainTask);
        countDownLatchManager.addCountDownLatch(mainTask.getTaskId());

        // 3. Wait
        try {
            countDownLatchManager.getCountDownLatch(mainTask.getTaskId()).await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 3-1. task-consumer
        // 등록된 sub-task, status 모두 ok -> task 결과를 Produce
        // 4. Task Result Consume
        // 받은 응답을 다시 countDownLatchManager를 통해서 결과 데이터를 받아야 해요.
        String result = countDownLatchManager.getDataForKey(mainTask.getTaskId());
        if (result.equals(SubTaskStatus.SUCCESS.getValue())) {
            // 4-1 Consume ok, Logic
            MemberMoneyJpaEntity memberMoneyJpaEntity = increaseMoneyPort.increaseMoney(
                    new MemberMoney.MembershipId(command.getTargetMembershipId()),
                    command.getAmount()
            );
            // 성공 시 멤버의 MemberMoney 값 증액이 필요해요.
            if (memberMoneyJpaEntity != null) {
                MoneyChangingRequestJpaEntity moneyChangingRequest = increaseMoneyPort.createIncreasingMoneyChangingRequest(
                        new MoneyChangingRequest.TargetMembershipId(command.getTargetMembershipId()),
                        new MoneyChangingRequest.ChangingType(ChangingMoneyType.INCREASING),
                        new MoneyChangingRequest.ChangingMoneyAmount(command.getAmount()),
                        new MoneyChangingRequest.ChangingStatus(ChangingMoneyStatus.SUCCEEDED),
                        new MoneyChangingRequest.Uuid(UUID.randomUUID().toString())
                );

                return moneyChangingRequestMapper.mapToDomainEntity(moneyChangingRequest);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    @Override
    public void increaseMoneyRequestByEvent(IncreaseMoneyRequestCommand command) {
        MemberMoneyJpaEntity memberMoneyJpaEntity = getMemberMoneyPort.getMemberMoney(
                new MemberMoney.MembershipId(command.getTargetMembershipId())
        );

        String aggregateIdentifier = memberMoneyJpaEntity.getAggregateIdentifier();

        // aggregateIdentifier가 없으면 새로 생성
        if (aggregateIdentifier == null || aggregateIdentifier.isEmpty()) {
            createAggregateAndIncrease(memberMoneyJpaEntity, command);
        } else {
            // aggregateIdentifier가 있으면 시도하되, Event Store에 없으면 재생성
            sendIncreaseMoneyCommand(aggregateIdentifier, memberMoneyJpaEntity, command);
        }
    }

    private void createAggregateAndIncrease(MemberMoneyJpaEntity memberMoneyJpaEntity, IncreaseMoneyRequestCommand command) {
        System.out.println("Creating new Aggregate for membershipId: " + command.getTargetMembershipId());
        
        commandGateway.send(new com.fastcampuspay.money.adapter.axon.command.MemberMoneyCreatedCommand(
                command.getTargetMembershipId()
        )).whenComplete((createResult, createThrowable) -> {
            if (createThrowable != null) {
                System.err.println("Failed to create MemberMoney Aggregate");
                createThrowable.printStackTrace();
                throw new RuntimeException("Failed to create MemberMoney Aggregate", createThrowable);
            } else {
                System.out.println("MemberMoney Aggregate created successfully: " + createResult);
                String newAggregateId = createResult.toString();
                
                // DB에 aggregateIdentifier 업데이트
                memberMoneyJpaEntity.setAggregateIdentifier(newAggregateId);
                
                // IncreaseMemberMoneyCommand 전송
                sendIncreaseMoneyCommandAfterCreate(newAggregateId, command);
            }
        });
    }

    private void sendIncreaseMoneyCommand(String aggregateIdentifier, MemberMoneyJpaEntity memberMoneyJpaEntity, IncreaseMoneyRequestCommand command) {
        commandGateway.send(IncreaseMemberMoneyCommand.builder()
                .aggregateIdentifier(aggregateIdentifier)
                .membershipId(command.getTargetMembershipId())
                .amount(command.getAmount())
                .build())
        .whenComplete((result, throwable) -> {
            if (throwable != null) {
                // Event Store에 Aggregate가 없는 경우 자동으로 재생성
                if (throwable.getCause() != null && 
                    throwable.getCause().getMessage() != null &&
                    throwable.getCause().getMessage().contains("not found in the event store")) {
                    
                    System.err.println("Aggregate not found in Event Store. Recreating...");
                    // aggregateIdentifier를 null로 설정하고 재생성
                    memberMoneyJpaEntity.setAggregateIdentifier(null);
                    createAggregateAndIncrease(memberMoneyJpaEntity, command);
                } else {
                    System.err.println("Failed to increase money");
                    throwable.printStackTrace();
                    throw new RuntimeException("Failed to increase money", throwable);
                }
            } else {
                System.out.println("Money increased successfully: " + result);
                increaseMoneyPort.increaseMoney(
                        new MemberMoney.MembershipId(command.getTargetMembershipId()),
                        command.getAmount()
                );
            }
        });
    }

    private void sendIncreaseMoneyCommandAfterCreate(String aggregateIdentifier, IncreaseMoneyRequestCommand command) {
        commandGateway.send(IncreaseMemberMoneyCommand.builder()
                .aggregateIdentifier(aggregateIdentifier)
                .membershipId(command.getTargetMembershipId())
                .amount(command.getAmount())
                .build())
        .whenComplete((result, throwable) -> {
            if (throwable != null) {
                System.err.println("Failed to increase money after creating aggregate");
                throwable.printStackTrace();
                throw new RuntimeException("Failed to increase money after creating aggregate", throwable);
            } else {
                System.out.println("Money increased successfully after creating aggregate: " + result);
                increaseMoneyPort.increaseMoney(
                        new MemberMoney.MembershipId(command.getTargetMembershipId()),
                        command.getAmount()
                );
            }
        });
    }
}
