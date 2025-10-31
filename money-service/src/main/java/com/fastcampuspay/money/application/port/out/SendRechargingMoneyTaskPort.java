package com.fastcampuspay.money.application.port.out;

import com.fastcampuspay.common.task.RechargingMoneyTask;

public interface SendRechargingMoneyTaskPort {
    void sendRechargingMoneyTaskPort(RechargingMoneyTask task);
}
