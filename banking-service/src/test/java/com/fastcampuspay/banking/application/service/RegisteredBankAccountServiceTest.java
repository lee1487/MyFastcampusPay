package com.fastcampuspay.banking.application.service;

import com.fastcampuspay.banking.adapter.in.web.RegisterBankAccountRequest;
import com.fastcampuspay.banking.domain.RegisteredBankAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class RegisteredBankAccountServiceTest {

    @Autowired
    private WebApplicationContext context; // ✅ 추가

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // ✅ 핵심!
                .alwaysDo(print())
                .build();
    }

    @Test
    public void testRegisterBankAccount() throws Exception {
        RegisterBankAccountRequest  request = new RegisterBankAccountRequest("1", "우리은행", "123");
        
        RegisteredBankAccount expect = RegisteredBankAccount.generateRegisteredBankAccount(
                new RegisteredBankAccount.RegisteredBankAccountId("1"),
                new RegisteredBankAccount.MembershipId("1"),
                new RegisteredBankAccount.BankName("우리은행"),
                new RegisteredBankAccount.BankAccountNumber("123"),
                new RegisteredBankAccount.LinkedStatusIsValid(true)
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/banking/account/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8") // ✅ 추가
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(mapper.writeValueAsString(expect)));
    }

}