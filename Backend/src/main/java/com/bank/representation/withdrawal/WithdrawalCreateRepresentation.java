package com.bank.representation.withdrawal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WithdrawalCreateRepresentation {
    @Positive
    @Max(1000)
    public Long amount;
    @NotNull
    public Integer account;
}
