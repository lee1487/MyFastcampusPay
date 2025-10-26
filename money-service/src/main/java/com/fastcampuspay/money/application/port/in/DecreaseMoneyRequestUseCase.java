package com.fastcampuspay.money.application.port.in;

import com.fastcampuspay.money.domain.MoneyChangingRequest;

public interface DecreaseMoneyRequestUseCase {
    MoneyChangingRequest decreaseMoneyRequest(DecreaseMoneyRequestCommand command);
}
