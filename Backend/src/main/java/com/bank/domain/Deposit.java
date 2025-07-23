package com.bank.domain;

import com.bank.representation.deposit.DepositCreateRepresentation;
import com.bank.util.Currency;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("DEPOSIT")
public class Deposit extends Transaction{
    public Deposit(Integer transactionId, LocalDateTime date, Money amount, Account account) {
        super(transactionId,date,amount,account);
    }

    public Deposit() {
    }

    public void updateDepositDetails(DepositCreateRepresentation depositCreateRepresentation,Account account){
        this.setAccount(account);
        this.setAmount(new Money(BigDecimal.valueOf(depositCreateRepresentation.amount), Currency.EUR));
        this.setDate(LocalDateTime.now());
    }
}

