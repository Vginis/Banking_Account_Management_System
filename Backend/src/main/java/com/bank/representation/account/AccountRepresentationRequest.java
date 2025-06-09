package com.bank.representation.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class AccountRepresentationRequest {
    @NotNull
    public Integer accountNumber;
    @NotNull
    public Integer userId;
    @PositiveOrZero
    public Integer balance;
}
