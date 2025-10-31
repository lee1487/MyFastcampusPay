package com.fastcampuspay.common.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RechargingMoneyTask {
    private String taskId;
    private String taskName;
    private String membershipId;
    private List<SubTask> subTaskList;
    private String toBankName;              // 법인 계좌
    private String toBankAccountNumber;     // 법인 계좌 번호
    private int moneyAmount;                // only won
}
