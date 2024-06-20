package com.bank.representation;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.util.List;

@RegisterReflectionForBinding
public class AccountRepresentation {
    public Integer accountNumber;
    public Integer userId;
    public Integer balance;
    public List<Long> cardList;
    public List<Integer> transactionList;
}
