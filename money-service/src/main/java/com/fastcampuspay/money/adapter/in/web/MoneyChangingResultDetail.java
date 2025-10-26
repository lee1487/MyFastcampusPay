package com.fastcampuspay.money.adapter.in.web;

import com.fastcampuspay.common.CnEnum.ChangingMoneyType;
import com.fastcampuspay.common.CnEnum.MoneyChangingResultStatus;
import com.fastcampuspay.money.domain.MoneyChangingRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyChangingResultDetail {
    private String moneyChangingRequestId;
    // 증액, 감액
    private ChangingMoneyType changingMoneyType;
    private MoneyChangingResultStatus moneyChangingResultStatus;
    private int amount;

    public static MoneyChangingResultDetail mapFromMoneyChangingRequest(MoneyChangingRequest request) {
        MoneyChangingResultStatus resultStatus;

        switch (request.getChangingMoneyStatus()) {
            case SUCCEEDED:
                resultStatus = MoneyChangingResultStatus.SUCCEEDED;
                break;
            case FAILED:
            case CANCELED:
                resultStatus = MoneyChangingResultStatus.FAILED;
                break;
            case REQUESTED:
            default:
                resultStatus = MoneyChangingResultStatus.FAILED;
                break;
        }

        return new MoneyChangingResultDetail(
                request.getMoneyChangingRequestId(),
                request.getChangingMoneyType(),
                resultStatus,
                request.getChangingMoneyAmount()
        );
    }
}
