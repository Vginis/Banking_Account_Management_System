package com.bank.util;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
public class Money implements Serializable {
    @Column(name="amount")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name="currency")
    private Currency currency;

    public Money(BigDecimal amount,Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Money() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}