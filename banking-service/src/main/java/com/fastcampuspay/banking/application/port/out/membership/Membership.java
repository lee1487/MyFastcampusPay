package com.fastcampuspay.banking.application.port.out.membership;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Membership {   // only for banking-service
    private String membershipId;
    private String name;
    private String email;
    private String address;
    private boolean isValid;
    private boolean isCorp;
}

