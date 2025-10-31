package com.fastcampuspay.money.adapter.out.service;

import com.fastcampuspay.common.client.CommonHttpClient;
import com.fastcampuspay.common.util.ObjectMapperUtil;
import com.fastcampuspay.money.application.port.out.membership.GetMembershipPort;
import com.fastcampuspay.money.application.port.out.membership.Membership;
import com.fastcampuspay.money.application.port.out.membership.MembershipStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;

@Component
public class MembershipServiceAdapter implements GetMembershipPort {
    private final CommonHttpClient commonHttpClient;
    private final String membershipServiceUrl;

    public MembershipServiceAdapter(CommonHttpClient commonHttpClient,
                                    @Value("${service.membership.url}") String membershipServiceUrl) {
        this.commonHttpClient = commonHttpClient;
        this.membershipServiceUrl = membershipServiceUrl;
    }

    @Override
    public MembershipStatus getMembership(String membershipId) {
        // 실제로 http Call
        String url = String.join("/", membershipServiceUrl, "membership", membershipId);
        HttpResponse<String> response = commonHttpClient.sendGetRequest(url);
        String jsonResponse = response.body();

        // json -> Membership (ObjectMapperUtil 사용)
        Membership membership = ObjectMapperUtil.fromJson(jsonResponse, Membership.class);
        if (membership.isValid()) {
            return new MembershipStatus(membership.getMembershipId(), true);
        }

        return null;
    }
}
