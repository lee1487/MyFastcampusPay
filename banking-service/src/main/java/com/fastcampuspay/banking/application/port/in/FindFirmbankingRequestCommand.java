package com.fastcampuspay.banking.application.port.in;

import com.fastcampuspay.common.SelfValidating;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class FindFirmbankingRequestCommand extends SelfValidating<FindFirmbankingRequestCommand> {
    @NotBlank
    private final String firmbankingRequestId;

    

    @Builder
    public FindFirmbankingRequestCommand(String firmbankingRequestId) {
        this.firmbankingRequestId = firmbankingRequestId;
        this.validateSelf();
    }
}
