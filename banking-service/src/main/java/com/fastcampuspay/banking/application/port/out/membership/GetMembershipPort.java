package com.fastcampuspay.banking.application.port.out.membership;

public interface GetMembershipPort {
    public MembershipStatus getMembership(String membershipId);
}
