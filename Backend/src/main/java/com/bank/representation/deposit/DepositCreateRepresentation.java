package com.bank.representation.deposit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DepositCreateRepresentation {
    @Positive
    public Long amount;
    @NotNull
    public Integer account;
}
