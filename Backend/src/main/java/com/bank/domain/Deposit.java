package com.bank.domain;

import com.bank.util.Currency;
import com.bank.util.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
@Entity
@DiscriminatorValue("DEPOSIT")
public class Deposit extends Transaction{
    public Deposit(Integer transactionId, LocalDateTime date, Money amount, Account account) {
        super(transactionId,date,amount,account);
    }

    public Deposit() {
    }

    public boolean makeDeposit(BigDecimal amount, Account account){
        if (account==null || amount == null){return false;}
        if (amount.compareTo(new BigDecimal(0))<= 0){
            return false;
        }
        BigDecimal currentAmount = account.getBalance().getAmount();
        BigDecimal newAmount = currentAmount.add(amount);
        account.setBalance(new Money(newAmount, Currency.EUR));
        account.getTransactionList().add(this);
        return true;
    }
}

