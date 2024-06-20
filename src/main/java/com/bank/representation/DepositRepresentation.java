package com.bank.representation;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.math.BigDecimal;

@RegisterReflectionForBinding
public class DepositRepresentation {
    public Integer transactionId;
    public String date;
    public BigDecimal amount;
    public Integer account;
}
