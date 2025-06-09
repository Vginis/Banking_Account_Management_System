package com.bank.representation.account;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.util.ArrayList;
import java.util.List;

@RegisterReflectionForBinding
public class AccountRepresentationResponse {
    public Integer accountNumber;
    public Integer userId;
    public Integer balance;
    public List<Long> cardList = new ArrayList<>();
    public List<Integer> transactionList = new ArrayList<>();
}
