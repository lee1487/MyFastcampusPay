package com.fastcampuspay.money.application.port.out.membership;


public interface GetMembershipPort {
    public MembershipStatus getMembership(String membershipId);
}
